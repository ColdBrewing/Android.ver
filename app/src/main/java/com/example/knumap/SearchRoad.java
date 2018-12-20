package com.example.knumap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchRoad extends AppCompatActivity {

    TextView nowPosition;
    String now;
    String dest;
    int destinationPosition;

    Spinner desti;
    Spinner desti_detail;

    private LocationManager locationManager;
    private LocationListener locationListener;
    double longitude;
    double latitude;

    ArrayAdapter<CharSequence> desti_list, desti_detail_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_load);


        nowPosition = findViewById(R.id.nowPosition);
        desti = findViewById(R.id.dest);
        desti_detail = findViewById(R.id.dest_detail);

        desti_list = ArrayAdapter.createFromResource(this, R.array.desti, android.R.layout.simple_spinner_dropdown_item);
        desti.setAdapter(desti_list);

        desti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (desti_list.getItem(i).equals("1")) {
                    desti_detail_list = ArrayAdapter.createFromResource(SearchRoad.this, R.array.zone1, android.R.layout.simple_spinner_dropdown_item);
                    desti_detail.setAdapter(desti_detail_list);
                    if (desti_detail.getVisibility() == View.GONE)
                        desti_detail.setVisibility(View.VISIBLE);
                }
                if (desti_list.getItem(i).equals("2")) {
                    desti_detail_list = ArrayAdapter.createFromResource(SearchRoad.this, R.array.zone2, android.R.layout.simple_spinner_dropdown_item);
                    desti_detail.setAdapter(desti_detail_list);
                    if (desti_detail.getVisibility() == View.GONE)
                        desti_detail.setVisibility(View.VISIBLE);
                }
                if (desti_list.getItem(i).equals("3")) {
                    desti_detail_list = ArrayAdapter.createFromResource(SearchRoad.this, R.array.zone3, android.R.layout.simple_spinner_dropdown_item);
                    desti_detail.setAdapter(desti_detail_list);
                    if (desti_detail.getVisibility() == View.GONE)
                        desti_detail.setVisibility(View.VISIBLE);
                }

                if (desti_list.getItem(i).equals("4")) {
                    desti_detail_list = ArrayAdapter.createFromResource(SearchRoad.this, R.array.zone4, android.R.layout.simple_spinner_dropdown_item);
                    desti_detail.setAdapter(desti_detail_list);
                    if (desti_detail.getVisibility() == View.GONE)
                        desti_detail.setVisibility(View.VISIBLE);
                }

                if(desti_list.getItem(i).equals("5"))
                {
                    desti_detail_list = ArrayAdapter.createFromResource(SearchRoad.this,R.array.zone5,android.R.layout.simple_spinner_dropdown_item);
                    desti_detail.setAdapter(desti_detail_list);
                    if(desti_detail.getVisibility()==View.GONE)
                        desti_detail.setVisibility(View.VISIBLE);
                }
                destinationPosition = 100* i;


                desti_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        dest = desti_detail_list.getItem(i).toString();

                        destinationPosition = destinationPosition/100 *100;
                        if(destinationPosition==100)
                            destinationPosition = destinationPosition + i;
                        else
                            destinationPosition = destinationPosition + i +1;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                if (desti_detail.getVisibility() == View.VISIBLE)
//                    desti_detail.setVisibility(View.GONE);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.i(" onCreate", "1");
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                Double La = Double.parseDouble(String.format("%.3f",latitude));
                Double Lo = Double.parseDouble(String.format("%.3f",longitude));

                now = "위도 : " + La + "\n경도 : " + Lo;
                nowPosition.setText(now);
                Log.i(" onLocationChanged", "1");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }


    //resetPosition 버튼 클릭 이벤트. 현위치 수정
    public void resetNowPosition(View view) {
        String locationProvider = LocationManager.GPS_PROVIDER;

        Log.i(" reset","1");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            //Log.i("Change","1");
            longitude = lastKnownLocation.getLongitude();
            latitude = lastKnownLocation.getLatitude();

            Double La = Double.parseDouble(String.format("%.3f",latitude));
            Double Lo = Double.parseDouble(String.format("%.3f",longitude));

            now = "위도 : " + La + "\n경도 : " + Lo;
            nowPosition.setText(now);
        }
    }

    //find버튼 클릭 이벤트
    public void FindRoute(View view)
    {
        Intent intent  = new Intent(SearchRoad.this,FindRoad.class);
        intent.putExtra("nowPosition",now);
        intent.putExtra("destination",dest);
        intent.putExtra("now_lat", latitude);
        intent.putExtra("now_long", longitude);
        intent.putExtra("destinationNum",destinationPosition);
        startActivity(intent);
    }

}
