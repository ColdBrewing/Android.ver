package com.example.knumap;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FindRoad extends AppCompatActivity {
    private static final String TAG = FindRoad.class.getSimpleName();
    private static ArrayList<RoadSpot> allSpot;
    private static ArrayList<Integer> routeNumList;
    private static ArrayList<RoadSpot> route;
    private static double now_lat;
    private static double now_lon;
    private static int destNum;
    private static double dest_lat;
    private static double dest_lon;

    SearchRoad searchRoad = new SearchRoad();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_load);
        Intent intent = getIntent();

        // 현 위치 GPS 정보 획득
        now_lat = intent.getDoubleExtra("now_lat", 0);
        now_lon = intent.getDoubleExtra("now_long", 0);
        // 목적지 건물번호 획득
        destNum = intent.getIntExtra("destinationNum", 0);

        route = new ArrayList<RoadSpot>();

        // 백그라운드 서버 통신작업
        ServerTask serverTask = new ServerTask(FindRoad.this);
        serverTask.execute();

        // 백그라운드 지도 작업
        MapTask mapTask = new MapTask(FindRoad.this);
        mapTask.execute();
    }

    private RoadSpot nearRoadSpot(double _curLatitude, double _curLongitude){
        LocationDistance ld = new LocationDistance();
        RoadSpot minRoadSpot = new RoadSpot();
        double minDistance = 999999;
        int count = 0;
        for(RoadSpot rs: allSpot){


            double temp = ld.distance(_curLatitude, _curLongitude,
                    rs.get_gps().latitude, rs.get_gps().longitude);
            if(temp > 100){
                continue;
            }
            if(temp < minDistance){
                minRoadSpot = rs;
                minDistance = temp;
            }
            count = count + 1;
        }

        return minRoadSpot;
    }

    public class LocationDistance{
        LocationDistance(){ }

        private double distance(double lat1, double lon1, double lat2, double lon2){
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                    + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1609.344;

            return dist;
        }
        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        // This function converts radians to decimal degrees
        private double rad2deg(double rad) {
            return (rad * 180 / Math.PI);
        }

    }
    private class ServerTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<FindRoad> mActivityWeakReference;

        ServerTask(FindRoad activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(Object... params) {

            ConnectionManager conn = new ConnectionManager();
            try {
                // 모든 분기점 정보 획득
                allSpot = conn.get_all_roadSpots();

                // 현위치-목적지 경로의 분기점 번호 배열 획득
                RoadSpot nearSpot = nearRoadSpot(now_lat, now_lon);
                String nearRoadSpotNum = String.valueOf(nearSpot.get_number());

                routeNumList = conn.get_route(nearRoadSpotNum, String.valueOf(destNum));


                // 경로 GPS 획득
                for(int temp:routeNumList){
                    for(RoadSpot rs : allSpot){
                        if(temp == rs.get_number()){
                            route.add(new RoadSpot(rs.get_number(),
                                    rs.get_gps().latitude, rs.get_gps().longitude,
                                    rs.get_connected()));
                        }
                    }
                }

                // 목적지 건물 GPS 정보 획득
                Building dest_b = conn.get_building_with_number(destNum);
                dest_lat = dest_b.get_gps().latitude;
                dest_lon = dest_b.get_gps().longitude;
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "a";
        }

        protected void onPostExecute(String result) {
            FindRoad activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {

            }
        }
    }

    private class MapTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<FindRoad> mActivityWeakReference;
        MapPolyline polyline = new MapPolyline();

        MapTask(FindRoad activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(Object... params) {


            return "a";
        }

        protected void onPostExecute(String result) {
            FindRoad activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                MapView mapView = new MapView(FindRoad.this);

                mapView.setDaumMapApiKey("mapApiKey");
                ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

                MapPoint stPoint = MapPoint.mapPointWithGeoCoord(now_lat, now_lon);
                mapView.setMapCenterPoint(stPoint, true);

                mapViewContainer.addView(mapView);

                //출발 마커
                MapPOIItem s_marker = new MapPOIItem();
                s_marker.setItemName("출발");
                s_marker.setTag(0);
                s_marker.setMapPoint(stPoint);
                // 기본으로 제공하는 RedPin 마커 모양.
                s_marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                s_marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                mapView.addPOIItem(s_marker);
                //mapView.selectPOIItem(s_marker, true);

                //도착 마커
                MapPoint destPoint = MapPoint.mapPointWithGeoCoord(dest_lat, dest_lon);
                MapPOIItem d_marker = new MapPOIItem();
                d_marker.setItemName("도착");
                d_marker.setTag(0);
                d_marker.setMapPoint(destPoint);
                // 기본으로 제공하는 BluePin 마커 모양.
                d_marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                d_marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                mapView.addPOIItem(d_marker);
                mapView.selectPOIItem(d_marker, true);

                polyline.setTag(1200);
                polyline.setLineColor(Color.argb(130, 218, 33, 39));
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(now_lat, now_lon));
                for(int idx = 0;idx<route.size();idx++) {
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(route.get(idx).get_gps().latitude, route.get(idx).get_gps().longitude));
                    MapPOIItem poiItem = new MapPOIItem();
                    poiItem.setTag(idx);

                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(route.get(idx).get_gps().latitude, route.get(idx).get_gps().longitude);
                    poiItem.setMapPoint(mapPoint);

                    mapView.addPOIItem(poiItem);
                }
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(dest_lat, dest_lon));

                mapView.addPolyline(polyline);

                //지도 중심점 설정(출 / 도착 중간점)
                MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                int padding = 100; // px
                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
            }
        }
    }