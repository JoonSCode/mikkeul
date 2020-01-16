package com.unimaginablecode.mikkeul;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.os.Bundle;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private Context context;
    TMapView tmap;
    private TMapGpsManager tmapgps = null;
    private boolean m_bTrackingMode = true;
    private boolean isFirst = true;
    private TMapMarkerItem markeritem = new TMapMarkerItem();
    private LinearLayout linearLayoutTmap;
    private ArrayList<TMapPoint> point_list;



    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BackgroundTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (findViewById(R.id.fab));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               tmap.setTrackingMode(true);
               tmap.setSightVisible(true);
            }
        });

        linearLayoutTmap = findViewById(R.id.linearLayoutTmap);
        tmap = new TMapView(this);
        point_list = new ArrayList<>();
        tmap.setSKTMapApiKey("l7xxe5e255e5c1824aaabb3813bd4dddbdfd");
        task = new BackgroundTask(MainActivity.this);
        task.execute();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            //현재위치의 좌표를 알수있는 부분
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                if(isFirst) {
                    tmap.setCenterPoint(longitude, latitude);
                    isFirst = false;
                }
                tmap.setLocationPoint(longitude, latitude);

                Log.d("TmapTest",""+longitude+","+latitude);


            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };


    public void setGps() {
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }


    private class BackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        ProgressDialog dialog;

        Context context;

        public BackgroundTask(Context context){

            this.context = context;


        }

        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            dialog = new ProgressDialog(context);

            dialog.show();

        }


        @Override
        protected Integer doInBackground(Integer... integers) {
            getPlace();
            while(point_list==null) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            linearLayoutTmap.addView(tmap);

            tmap.setIconVisibility(true);//현재위치로 표시될 아이콘을 표시할지 여부를 설정합니다.

            tmap.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
                @Override
                public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                    Log.d("scroll test", "스크롤 됨");
                    tmap.removeAllMarkerItem();
                    for(TMapPoint point: point_list) {
                        boolean result = tmap.isValidTMapPoint(point);

                        if (result == true) {
                            markeritem.setTMapPoint(point);
                            tmap.addMarkerItem("TestID", markeritem);
                            Log.d("point test", "현재 위치에서 표시할 수 있음   위도: " + point.getLongitude() + "   경도: " + point.getLatitude());
                        } else {
                            Log.d("point test", "현재 위치에서 표시할 수 없음   위도: " + point.getLongitude() + "   경도: " + point.getLatitude());
                        }
                    }
                }
            });

            setGps();

        }
    }

    protected void getPlace(){
        db.collection("Place").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        point_list.add(new TMapPoint(document.getGeoPoint("location").getLatitude(), document.getGeoPoint("location").getLongitude()));
                    }
                } else {
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

}



