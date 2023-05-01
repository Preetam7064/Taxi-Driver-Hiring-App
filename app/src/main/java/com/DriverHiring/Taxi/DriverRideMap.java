package com.DriverHiring.Taxi;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.DriverHiring.Taxi.AllPostsWork.ShowRouteOnMap;
import com.DriverHiring.Taxi.Helper.DirectionJSONParser;
import com.DriverHiring.Taxi.HomePageMap;
import com.DriverHiring.Taxi.ModelClasses.NewPostRider;
import com.DriverHiring.Taxi.ModelClasses.NewRiderModel;
import com.DriverHiring.Taxi.ModelClasses.PostDriver;
import com.DriverHiring.Taxi.ModelClasses.RequestsModel;
import com.DriverHiring.Taxi.ModelClasses.RiderModel;
import com.DriverHiring.Taxi.ModelClasses.StartRideRequestModel;
import com.DriverHiring.Taxi.Services.IGoogleAPI;
import com.DriverHiring.Taxi.Services.RetrofitClient;
import com.DriverHiring.Taxi.TripDetails;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AlertDialog;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverRideMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String baseURL = "https://maps.googleapis.com";
    private static final int[] COLORS = new int[]{R.color.primary_dark1, R.color.primary1, R.color.primary_light1, R.color.accent1, R.color.primary_dark};
    //new
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    String startL, startLN, endL, endLN, title = "";
    LatLng start, waypoint, end;
    Polyline direction;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    IGoogleAPI mService;
    Button endrideforpostbtn;
    String postId;
    String startPoint, endPoint, riderPickPoint, riderId;
    Button btnStartRide, btnEndRide;
    private GoogleMap mMap;
    private List<Polyline> polylines;
    private LocationRequest locationRequest;
    private Location mLastLocation;
    private Marker driverMarker;
    private String currentUserUid, type, fullname;
    private DatabaseReference myRef;
    private String keyToStopRide;
    //details to be pdated for routing
    private String driverLat, driverLng, customerLat, customerLng, endPointLat, endPointLng;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private LocationManager locationManager;
    private boolean isPermission;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ride_map);

        if (requestSinglePermission()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            //it was pre written
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkLocation(); //check whether location service is enable or not in your  phone
        }

        SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);
        currentUserUid = prefs.getString("uid", "");
        type = prefs.getString("type", "");
        fullname = prefs.getString("fullname", "");
        postId = getIntent().getStringExtra("myid");
        //Toast.makeText(DriverRideMap.this, ""+currentUserUid, Toast.LENGTH_SHORT).show();
        myRef = FirebaseDatabase.getInstance().getReference("Accepted Requests").child(currentUserUid);
        //Toast.makeText(DriverRideMap.this, ""+myRef, Toast.LENGTH_SHORT).show();
        btnStartRide = findViewById(R.id.startrideforpost);
        btnEndRide = findViewById(R.id.endrideforpost);


        if (myRef != null) {
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    double latitude = 0, longitude = 0;

                    //to avoid nuul exception

                    RequestsModel value = dataSnapshot.getValue(RequestsModel.class);
                    riderId = value.getReciverid();
                    Geocoder geocoder = new Geocoder(DriverRideMap.this);
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocationName(value.getEndpoint(), 1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (addresses != null && !addresses.isEmpty()) {
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                        // Do something with latitude and longitude
                    }
                    endPointLat = Double.toString(latitude);
                    endPointLng = Double.toString(longitude);
                    //Toast.makeText(DriverRideMap.this, "" + latitude, Toast.LENGTH_SHORT).show();
                    if (riderId != null) {
                        databaseReference(riderId);
                    }
                    //databaseReference();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        btnStartRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (driverLat != null && driverLng != null && customerLat != null && customerLng != null && endPointLat != null && endPointLng != null) {
                    Intent i = new Intent(DriverRideMap.this, ShowRouteOnMap.class);
                    i.putExtra("latstart", driverLat);
                    i.putExtra("lngstart", driverLng);
                    i.putExtra("customerLat", customerLat);
                    i.putExtra("customerLng", customerLng);
                    i.putExtra("latend", endPointLat);
                    i.putExtra("lngend", endPointLng);
                    i.putExtra("title", "request");
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(DriverRideMap.this, "Wait for Database to fetch data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkLocation() {
        if (!isLocationEnabled()) showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location").setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app").setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        try {
            boolean isSucess = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map));
            if (!isSucess) {
                Toast.makeText(this, "Map style error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

        mMap = googleMap;

        //new settings for map
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);

    }

    public void databaseReference(String riderID) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Riders").child(riderID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //RiderModel newRiderModel=new RiderModel(dataSnapshot.getValue(String.class));
                //ArrayList<String> arrayList = new ArrayList<>();
                //String value = dataSnapshot.getValue(String.class);
                HashMap<String, String> value = new HashMap<String, String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    value.put(childSnapshot.getKey(), childSnapshot.getValue().toString());
                }
                //value.put(dataSnapshot.getValue(String.class));
                //Toast.makeText(DriverRideMap.this, "" + value.get("currentlatitude"), Toast.LENGTH_SHORT).show();
                customerLat = value.get("currentlatitude");
                customerLng = value.get("currentlogitude");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(UPDATE_INTERVAL).setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
        driverLat = Double.toString(location.getLatitude());
        driverLng = Double.toString(location.getLongitude());
        //Toast.makeText(this, "Location Changed" + driverLat.toString(), Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //it was pre written
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed. Error: " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }

        }
    }

    private boolean requestSinglePermission() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.CALL_PHONE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                if (report.isAnyPermissionPermanentlyDenied()) {
                    // check for permanent denial of permission

                    isPermission = false;


                    final AlertDialog.Builder dialog = new AlertDialog.Builder(DriverRideMap.this);
                    dialog.setTitle("Permissions").setMessage("You have to give all permissions!!, the app will close now")

                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    finishAffinity();
                                }
                            });
                    dialog.setCancelable(false);
                    dialog.show();

                } else if (report.areAllPermissionsGranted()) {

                    //Single Permission is granted
                    Toast.makeText(getApplicationContext(), "permissions are granted!", Toast.LENGTH_SHORT).show();
                    isPermission = true;

                }


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();


        return isPermission;

    }
}