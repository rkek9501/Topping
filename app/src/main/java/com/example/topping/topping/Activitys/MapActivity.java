package com.example.topping.topping.Activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topping.topping.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button searchBtn, choiceBtn;
    private EditText editText;
    private TextView mapText;

    private boolean markerCheck = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_map);
        editText = (EditText)findViewById(R.id.mapSearch);
        searchBtn = (Button)findViewById(R.id.mapSearchBtn);
        mapText = (TextView)findViewById(R.id.mapText) ;
        choiceBtn = (Button)findViewById(R.id.choiceBtn);
        choiceBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(markerCheck) {
                    Intent intent = new Intent();
                    intent.putExtra("result", mapText.getText());
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "장소가 선택되지 않았습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(!markerCheck){
                    MarkerOptions mOptions = new MarkerOptions();
                    // 마커 타이틀
//                    mOptions.title("마커 좌표");
                    Double latitude = latLng.latitude; // 위도
                    Double longitude = latLng.longitude; // 경도
                    // 마커의 스니펫(간단한 텍스트) 설정
//                    mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                    // LatLng: 위도 경도 쌍을 나타냄
                    mOptions.position(new LatLng(latitude, longitude));
                    // 마커(핀) 추가
                    googleMap.addMarker(mOptions);
                    List<Address> address;
                    markerCheck = true;
                    try {
                        address = geocoder.getFromLocation(latitude,longitude, 1);
                        String realAddr = address.get(0).getAddressLine(0);

                        mapText.setText(realAddr/*+", \n위도 : "+latitude+", 경도 : "+longitude*/);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "생성된 마커를 먼저 지워주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("marker", marker+"");
                marker.remove();
                markerCheck =false;
                mapText.setText("");
                return true;
            }
        });
        searchBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!markerCheck) {

                        String address = editText.getText().toString();
                        if(address.equals("")){
                            Toast.makeText(getApplicationContext(), "입력된 값이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        List<Address> addressList = null;

                        // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                        addressList = geocoder.getFromLocationName(address,1);

                        Double latitude = addressList.get(0).getLatitude(); // 위도
                        Double longitude = addressList.get(0).getLongitude(); // 경도

                        String realAddr = addressList.get(0).getAddressLine(0);
                        mapText.setText(realAddr/* + ", \n위도 : " + latitude + ", 경도 : " + longitude*/);
                        // 좌표(위도, 경도) 생성
                        LatLng point = new LatLng(latitude,longitude);
                        // 마커 생성
                        MarkerOptions mOptions2 = new MarkerOptions();
                        mOptions2.title("search result");
                        mOptions2.snippet(realAddr);
                        mOptions2.position(point);
                        markerCheck = true;
                        // 마커 추가
                        mMap.addMarker(mOptions2);
                        // 해당 좌표로 화면 줌
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                    } else {
                        Toast.makeText(getApplicationContext(), "생성된 마커를 먼저 지우고 검색해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }catch (IndexOutOfBoundsException e){
                    markerCheck =false;
                    Toast.makeText(getApplicationContext(), "검색결과가 없습니다. 다시 검색해 주세요.", Toast.LENGTH_SHORT).show();
                }catch (NullPointerException e) {
                    markerCheck =false;
                    Toast.makeText(getApplicationContext(), "검색결과가 없습니다. 다시 검색해 주세요.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        LatLng anyang = new LatLng(37.401291,126.922888);
//        mMap.addMarker(new MarkerOptions().position(anyang).title("안양시, 안양역"));
        CameraPosition position = new CameraPosition.Builder().target(new LatLng(37.39444, 126.95556)).zoom(14).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(anyang));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

}
