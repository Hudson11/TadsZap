package com.example.hudson.tadszap.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hudson.tadszap.Modelo.Mensagem;
import com.example.hudson.tadszap.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.concurrent.Executor;

import static android.content.Context.MODE_PRIVATE;

public class Fragment3 extends Fragment implements OnMapReadyCallback{

    private SupportMapFragment mFragment;
    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocation;

    private FirebaseDatabase mFirebase;
    private DatabaseReference mReference;

    private static final Integer REQUEST_LOCATION = 70;
    private static final String[] permissionMaps = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                                Manifest.permission.ACCESS_COARSE_LOCATION};
    private String linkMapa;
    private String mUsername;

    //Required public constructor
    public Fragment3() {

    }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle saveInstanceState){
        View v = inflate.inflate(R.layout.fragment3, container, false);

        mFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);

        mFusedLocation = LocationServices.getFusedLocationProviderClient(getContext());

        mFirebase = FirebaseDatabase.getInstance();
        mReference = mFirebase.getReference().child("mensagens");

        SharedPreferences prefs = getActivity().getSharedPreferences("PREFS-NAME", MODE_PRIVATE);
        mUsername = prefs.getString("username", "Não Conhecido");
        Log.i("User", ""+mUsername);

        FloatingActionButton enviarLocal = v.findViewById(R.id.floatingActionButton);
        enviarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), permissionMaps[0]) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocation.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            if (location != null){
                                linkMapa = "https://www.google.com.br/maps/place/" + latitude + "," + longitude;

                                Mensagem mensagem = new Mensagem(linkMapa,new Date(), mUsername, null);

                                mReference.push().setValue(mensagem);
                                Toast.makeText(getContext(), "Localização Enviada", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getContext(), "Erro", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (ContextCompat.checkSelfPermission(getContext(), permissionMaps[0]) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 70:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), permissionMaps[0]) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                }
        }
    }

}
