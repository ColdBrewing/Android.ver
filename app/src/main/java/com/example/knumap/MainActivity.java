package com.example.knumap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    private Button captureBuilding;
    private Button findLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        // 사용자에게 접근권한여부 확인
        if (permissionCamera == PackageManager.PERMISSION_DENIED||
                permissionCheck1 == PackageManager.PERMISSION_DENIED||
                permissionCheck2 == PackageManager.PERMISSION_DENIED||
                permissionCheck3 == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

        setContentView(R.layout.activity_main);

        captureBuilding = findViewById(R.id.buildingNum);
        captureBuilding.setOnClickListener(view -> goCaptureBuilding(view));

        findLoad = findViewById(R.id.findLoad);
        findLoad.setOnClickListener(view -> goFindRoad(view));

        try{
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = new String(Base64.encode(md.digest(), 0));
                Log.i("Hash key:", "!!!!!!!"+key+"!!!!!!");
            }
        } catch (Exception e){
            Log.e("name not found", e.toString());
        }
    }

    public void goCaptureBuilding(View view)
    {
        Intent intent = new Intent(MainActivity.this,BuildingResult.class);

        startActivity(intent);
    }

    public void goFindRoad(View view)
    {
        Intent intent = new Intent(MainActivity.this,SearchRoad.class);

        startActivity(intent);
    }
}
