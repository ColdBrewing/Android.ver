package com.example.knumap;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;
import java.util.Iterator;


public class Building implements Unit{
    private int number;
    private String name;
    private GPS gps;
    private String imageUrl;
    private static JSONParser jsonParser= new JSONParser();

    public Building(int _number, String _name, double _latitude, double _longitude, String _imageUrl){
        this.number = _number;
        this.name = _name;
        this.gps = new GPS(_latitude, _longitude);
        this.imageUrl = _imageUrl;
    }

    public Building(int _number,
                    String _name,
                    double _latitude,
                    double _longitude,
                    ArrayList<String> _colleges){
        this.number = _number;
        this.name = _name;
        this.gps = new GPS(_latitude, _longitude);
        this.imageUrl = "";
    }

    public Building(int _number,
                    String _name,
                    double _latitude,
                    double _longitude,
                    ArrayList<String> _colleges,
                    String _imageUrl){
        this.number = _number;
        this.name = _name;
        this.gps = new GPS(_latitude, _longitude);
        this.imageUrl = _imageUrl;
    }

    public Building( String _jsonDataString) throws ParseException {
        final JSONObject _rawData = (JSONObject) jsonParser.parse(_jsonDataString);
        final JSONObject _data = (JSONObject) _rawData.get("data");

        final JSONObject _gps = (JSONObject) _data.get("gps");
        final JSONArray _colleges = (JSONArray) _data.get("colleges");
        System.out.println("Parsing json string to object: " + _jsonDataString);

        this.number = Integer.parseInt( _data.get("number").toString());
        this.name = (String) _data.get("name");
        this.gps = new GPS((Double) _gps.get("latitude"),
                            (Double) _gps.get("longitude"));
        this.imageUrl = (String) _data.get("imageUrl");
    }

    public int get_number(){ return this.number;}
    public String get_name(){ return this.name;}
    public GPS get_gps(){ return this.gps;}
    public String get_imageUrl(){ return this.imageUrl;}

    public static ArrayList<Building> get_listified(String _jsonArrayDataString) throws ParseException {
        ArrayList<Building> _retDataList = new ArrayList<Building>();
        JSONObject _jsonData = (JSONObject) jsonParser.parse(_jsonArrayDataString);
        final JSONArray _jsonList = (JSONArray) _jsonData.get("data");

        for( int idx = 0; idx < _jsonList.size(); idx++){
            String _buildingStringData = _jsonList.get(idx).toString();
            _retDataList.add( new Building( _buildingStringData));
        }

        return _retDataList;
    }

    @Override
    public String toString() {
        return String.format("{number: %s, name: %s, gps: %s, imageUrl: %s}",
                this.number, this.name, this.gps.toString(), this.imageUrl);
    }
}
