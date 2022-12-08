package uk.ac.tees.a0174604.carpital;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LastLocationRequest;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//public class SearchFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

public class SearchFragment extends Fragment implements  OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context mContext;
    private static SearchFragment unique;
    private boolean isPermissionGranted;
    GoogleMap myGoogleMap;
    public static FloatingActionButton fab;
    private FusedLocationProviderClient myLocationClient;
    private SupportMapFragment supportMapFragment;

    private String LOG = SearchFragment.class.getSimpleName();

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ////        create floating to pin point map location
        fab = new FloatingActionButton(getActivity());
        RelativeLayout layout = getActivity().findViewById(R.id.rootContainer);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,24,300);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        fab.setLayoutParams(params);

        if (layout != null){
            layout.addView(fab);
        }
        fab.setImageResource(android.R.drawable.ic_dialog_map);

        checkMyPermission();

        myLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                get user current location
                getCurrLoc();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(isPermissionGranted) {
            supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_container);
            if (supportMapFragment == null) {
                supportMapFragment = SupportMapFragment.newInstance();
                supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_container);
            }

            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myGoogleMap = googleMap;
        Log.d(LOG, String.valueOf(myGoogleMap));
//        myGoogleMap.setMyLocationEnabled(true);
    }



    @SuppressLint("MissingPermission")
    private void getCurrLoc() {

        myLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Location location = task.getResult();
                Log.d(LOG, String.valueOf(location.getLatitude()));
                Log.d(LOG,  String.valueOf(location.getLongitude()));
                Log.d(LOG, String.valueOf(myGoogleMap));
                gotoLocation(location.getLatitude(), location.getLongitude());


            }
        });
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng LatLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng, 18);
        Log.d(LOG, String.valueOf(myGoogleMap));
        if (myGoogleMap != null) {
            myGoogleMap.addMarker(new MarkerOptions().position(LatLng).title("Current Location"));
            myGoogleMap.moveCamera(cameraUpdate);
            myGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        }

    }


//    }

    private void checkMyPermission() {

        Dexter.withContext(getActivity()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                isPermissionGranted =  true;

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.cancelPermissionRequest();
            }
        }).check();
    }


    //    use singleton pattern to instantiate the fragment
    public static SearchFragment newInstance(){
        if (unique == null){
            unique = new SearchFragment();
        }
        return unique;
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}