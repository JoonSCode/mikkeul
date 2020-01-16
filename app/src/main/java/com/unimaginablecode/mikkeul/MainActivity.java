package com.unimaginablecode.mikkeul;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.os.Bundle;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.unimaginablecode.mikkeul.R;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    private Context context;
    private  TMapView tMapView;
    private TMapGpsManager tmapgps = null;
    private boolean m_bTrackingMode = true;
    private double nowLogitude;
    private double nowLatitude;


    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            nowLogitude=location.getLongitude();
            nowLatitude=location.getLatitude();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setSKTMapApiKey("l7xxe5e255e5c1824aaabb3813bd4dddbdfd");//API key
        tMapView.setIconVisibility(true);//현재 위치 아이콘 표시

        //////////////이거 현재위치 추가/////////////////////////////////////
        tmapgps = new TMapGpsManager(MainActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); //연결된 인터넷으로 현 위치를 받습니다.
        //실내일 때 유용합니다.
        //tmapgps.setProvider(tmapgps.GPS_PROVIDER); //gps로 현 위치를 잡습니다.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }
        tmapgps.OpenGps();

        /*  화면중심을 단말의 현재위치로 이동 */
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);
        //현재 위치
        TMapPoint point2 = tmapgps.getLocation();


        //////////////이거 현재위치 추가////////////////////////////////////

        linearLayoutTmap.addView(tMapView);
        context = getApplicationContext();


    }


}




