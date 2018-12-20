package com.example.knumap;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ConnectionManager {
    private static final String API_SERVER_HOST = "http://49.236.134.66:3389";
    private static final String API_BASE_PATH = "/api";
    private static final String HttpMethodType = "GET";
    private static final String BUILDING_PATH = "/building";
    private static final String ROADSPOT_PATH = "/roadspot";
    private static final String ROUTE_PATH = "/path?";
    private static final String FROM_PATH = "from=";
    private static final String TO_PATH = "to=";

    public ConnectionManager(){}

    public ArrayList<Building> get_all_buildings() throws ParseException, JSONException {
        return Building.get_listified( request(BUILDING_PATH));
    }
    public ArrayList<RoadSpot> get_all_roadSpots() throws ParseException, JSONException {
        return RoadSpot.get_listified( request(ROADSPOT_PATH));
    }
    public Building get_building_with_number(int _number) throws ParseException, JSONException {
        return new Building( request(BUILDING_PATH + "/" + _number));
    }
    public RoadSpot get_roadSpot_with_number(String _number) throws ParseException, JSONException {
        return new RoadSpot( request( ROADSPOT_PATH + "/" + _number));
    }

    public ArrayList<Integer> get_route(String _fromSpot, String _toBuilding) throws ParseException, JSONException{
        ArrayList<Integer> route = new ArrayList<Integer>();
        JSONParser jsonParser= new JSONParser();
        String _jsonArrayDataString = request(ROUTE_PATH + FROM_PATH + _fromSpot + "&" + TO_PATH + _toBuilding);
        JSONObject _jsonData = (JSONObject) jsonParser.parse(_jsonArrayDataString);

        final JSONArray _jsonList = (JSONArray) _jsonData.get("path");

        for( int idx = 0; idx < _jsonList.size(); idx++) {
            int _routeSpotNum = Integer.parseInt(_jsonList.get(idx).toString());
            route.add(_routeSpotNum);
        }
        return route;
    }

    private String request(final String apiPath){
        /*
            Client only use Get method
         */
        String requestUri = API_SERVER_HOST + API_BASE_PATH + apiPath;

        HttpURLConnection CONN;
        InputStreamReader INPUT_READER = null;
        BufferedReader BUF_READER = null;

        try{
            final URL url= new URL( requestUri);
            CONN = (HttpURLConnection) url.openConnection();
            CONN.setRequestMethod( HttpMethodType);
            /* add Authorization in here ! */
            CONN.setAllowUserInteraction(false);
            //CONN.setDoOutput(true);//주석처리 풀면 POST로 보냄-> 500 에러

            CONN.setRequestProperty("Content-Type", "application/json");
            CONN.setRequestProperty("charset", "utf-8");

            final int responseCode = CONN.getResponseCode();
            System.out.println(String.format("Request[%s] to URI: %s", HttpMethodType, url));
            System.out.println("Response Code: " + responseCode);
            if( responseCode == 200)
                /* Success */
                INPUT_READER = new InputStreamReader( CONN.getInputStream());
            else
                INPUT_READER = new InputStreamReader( CONN.getErrorStream());

            /* Read string data from input stream */
            BUF_READER = new BufferedReader( INPUT_READER);
            final StringBuffer BUFFER = new StringBuffer();
            String dataLine;
            while( (dataLine = BUF_READER.readLine()) != null) {
                BUFFER.append(dataLine);
            }

            return BUFFER.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}