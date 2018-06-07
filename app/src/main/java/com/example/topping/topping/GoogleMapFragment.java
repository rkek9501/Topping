package com.example.topping.topping;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap mMap;
    private Geocoder geocoder;
    private CameraPosition mCameraPosition;

    //기기의 현재 위치
    private Location currentLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    MapView mapView;
    String mPlace;
    public GoogleMapFragment() {
        // Required empty public constructor
    }

    public static GoogleMapFragment newInstance(String place){
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Place", place);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_map, container, false);
        if (savedInstanceState != null) {
            currentLocation
                    = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        mapView = (MapView)view.findViewById(R.id.gMap);
        mapView.getMapAsync(this);

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //액티비티가 처음 생성될 때 실행되는 함수

        if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);

        geocoder = new Geocoder(getContext());
        List<Address> addressList = null;

        try {
            addressList = geocoder.getFromLocationName(getArguments().getString("Place").toString(),1);
            Double latitude = addressList.get(0).getLatitude(); // 위도
            Double longitude = addressList.get(0).getLongitude(); // 경도

            LatLng point = new LatLng(latitude,longitude);
            String realAddr = addressList.get(0).getAddressLine(0);
            // 마커 생성
            MarkerOptions mOptions2 = new MarkerOptions();
            mOptions2.title(mPlace).snippet(realAddr).position(point);
            // 마커 추가
            mMap.addMarker(mOptions2);
            // 해당 좌표로 화면 줌
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

            /*CameraPosition position = new CameraPosition.Builder().target(new LatLng(latitude,longitude).zoom(12).build());
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));*/

        }catch (IndexOutOfBoundsException e) {
            LatLng point = new LatLng(37.4032943,126.93039180000005);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
        }catch (IOException e) {
            e.printStackTrace();
        }


        // GoogleMap 에 마커클릭 이벤트 설정 가능.
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 마커 클릭시 호출되는 콜백 메서드
                Toast.makeText(getContext(),marker.getTitle() + " 클릭했음", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }
}
