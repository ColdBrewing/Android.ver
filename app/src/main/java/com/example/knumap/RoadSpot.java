package com.example.knumap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;

import java.util.Iterator;

public class RoadSpot implements Unit{
    private int number;
    private GPS gps;
    private ArrayList<Integer> connected;
    private static JSONParser jsonParser= new JSONParser();

    public RoadSpot(int _number, double _latitude, double _longitude){
        this.number = _number;
        this.gps = new GPS( _latitude, _longitude);
        this.connected = new ArrayList<Integer>();
    }

    public RoadSpot(int _number,
                    double _latitude,
                    double _longitude,
                    ArrayList<Integer> _connected){
        this.number = _number;
        this.gps = new GPS(_latitude, _longitude);
        this.connected = _connected;
    }

    public RoadSpot(String _jsonDataString) throws ParseException {
        final JSONObject _data = (JSONObject) jsonParser.parse( _jsonDataString);
        final JSONObject _gps = (JSONObject) _data.get("gps");
        final JSONArray _connected = (JSONArray) _data.get("connected");
        System.out.println("Parsing json string to object: " + _jsonDataString);

        this.number = Integer.parseInt( _data.get("number").toString());
        this.gps = new GPS( (double) _gps.get("latitude"),
                (double) _gps.get("longitude"));
        this.connected = new ArrayList<Integer>();
        for( int idx = 0; idx < _connected.size(); idx++)
            this.connected.add( Integer.parseInt( _connected.get(idx).toString()) );
    }

    public RoadSpot() {

    }

    public int get_number(){ return this.number;}
    public GPS get_gps(){ return this.gps;}
    public ArrayList<Integer> get_connected(){ return this.connected;}
    // private Iterator<Integer> get_list_iterator() { return this.connected.iterator();}

    public static ArrayList<RoadSpot> get_listified(String _jsonArrayDataString) throws ParseException {
        ArrayList<RoadSpot> _retDataList = new ArrayList<RoadSpot>();
        JSONObject _jsonData = (JSONObject) jsonParser.parse(_jsonArrayDataString);
        final JSONArray _jsonList = (JSONArray) _jsonData.get("data");

        for( int idx = 0; idx < _jsonList.size(); idx++) {
            String _roadSpotStringData = _jsonList.get(idx).toString();
            _retDataList.add( new RoadSpot( _roadSpotStringData));
        }

        return _retDataList;
    }

    @Override
    public String toString() {
        return String.format("{number: %s, gps: %s, connected: %s}", this.number, this.gps.toString(), this.connected.toString());
    }
}