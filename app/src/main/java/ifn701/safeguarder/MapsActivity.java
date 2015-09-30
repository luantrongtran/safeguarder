/**
 * References http://android-developers.blogspot.com.au/2011/06/deep-dive-into-location.html
 */
package ifn701.safeguarder;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import ifn701.safeguarder.activities.CustomWindowInfoAdapter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;
import ifn701.safeguarder.Observation.ObservationList;
import ifn701.safeguarder.Parcelable.AccidentListParcelable;
import ifn701.safeguarder.activities.EventFilterActivity;
import ifn701.safeguarder.activities.LeftMenuAdapter;
import ifn701.safeguarder.activities.ReportActivity;
import ifn701.safeguarder.activities.ZoneSettingActivity;
import ifn701.safeguarder.backend.myApi.model.AccidentList;
import ifn701.safeguarder.backgroundservices.LocationAutoTracker;
import ifn701.safeguarder.backgroundservices.LocationTrackerService;
import ifn701.safeguarder.backgroundservices.UpdateAccidentInRangeReceiver;
import ifn701.safeguarder.backgroundservices.UpdateAccidentsInRangeService;
import ifn701.safeguarder.entities.google_places.PlacesList;
import ifn701.safeguarder.webservices.google_cloud_service.RegistrationIntentService;
import ifn701.safeguarder.webservices.google_web_services.GooglePlacesSearch;
import ifn701.safeguarder.webservices.google_web_services.IGooglePlacesSearch;
import ifn701.safeguarder.webservices.IUpdateAccidentInRange;
import ifn701.safeguarder.webservices.UpdateAccidentsInRange;


public class MapsActivity extends AppCompatActivity
        implements ConnectionCallbacks, OnConnectionFailedListener, IUpdateAccidentInRange,
        IGooglePlacesSearch{

    public static int requestCodeObsDetailed = 2;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient googleApiClient;
    LocationRequest mLocationRequest;

    public long updateCurrentLocationInterval = 13*1000;//10s
    private AlarmManager alarmManager;
    private PendingIntent currentLocationPendingIntent;

    public long updateAccidentListInterval = 10*1000;//10s
    private PendingIntent updateAccidentListPendingIntent;

    public static AccidentManager accidentManager;
    public static HealthServicesManager healthServicesmanager = new HealthServicesManager();
    public static UserDrawer userDrawer;
    boolean isLocationSwitcherShowed = false;

    UserSettingsPreferences userSettingsPreferences;

    //Navigation menu
    private Toolbar toolbar;
    private TextView switchLocationText;

    RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mLeftMenuAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout leftMenuDrawer;

    LinearLayout locationSwitcher;

//    ActionBarDrawerToggle mDrawerToggle;
    //end navigation menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // mapFragment.getMapAsync(this);

        setUpGoogleApiClient();
        setUpComponents();

//        setUpDummyData();

        registerReceiver();

        setUpNavigationMenu();

        setUpGoogleCloudMessage();

        accidentManager = new AccidentManager(getApplicationContext());

        setDefaultSharedPreferences();
    }

    @Override
    protected void onStart() {
        super.onStart();

        googleApiClient.connect();
        updateMapFooter();
    }

    @Override
    protected void onStop() {
        cancelServiceOnStop();
        googleApiClient.disconnect();
        super.onStop();
    }

    public void setUpGoogleCloudMessage() {
        if(checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class) ;
            startService(intent);
        }
    }

    public void setDefaultSharedPreferences() {
        //Set default values for Event Filter settings
//        PreferenceManager.setDefaultValues(this, R.xml.event_filter_default_settings, false);
    }

    public void setUpGoogleApiClient() {
        createLocationRequest();//set up for location request

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    protected void setUpComponents() {
        locationSwitcher = (LinearLayout) findViewById(R.id.locationSwitcherBar);
        switchLocationText = (TextView) findViewById(R.id.switchLocationText);
        userSettingsPreferences = new UserSettingsPreferences(getApplicationContext());

        findViewById(R.id.currentLocationButton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(Constants.APPLICATION_ID, "Pan to my location");
                panToMyLocation();
                switchLocationBar();
                return false;
            }
        });

        findViewById(R.id.homeLocationButton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(Constants.APPLICATION_ID, "Pan to home location");
                panToHomeLocation();
                switchLocationBar();
                return false;
            }
        });
    }

    /**
     * Register all necessary receivers
     */
    public void registerReceiver() {
        registerAccidentListUpdateReceiver();
        registerCurrentLocationUpdatedReceiver();
    }

    public void registerAccidentListUpdateReceiver() {
        IntentFilter onAccidentListUpdatedFilter
                = new IntentFilter(UpdateAccidentsInRangeService.ACTION);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(onAccidentListUpdated, onAccidentListUpdatedFilter);
    }

    public void registerCurrentLocationUpdatedReceiver() {
        IntentFilter onCurrentLocationUpdatedFilter
                = new IntentFilter(LocationTrackerService.ACTION);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(onCurrentLocationUpdated, onCurrentLocationUpdatedFilter);
    }

    public void setUpDummyData() {
        //Setup dummy data for logging in information.
        UserInfoPreferences userInfoPrefs = new UserInfoPreferences(getApplicationContext());
        userInfoPrefs.setUserId(1);
        userInfoPrefs.setProfilePicture("https://s-media-cache-ak0.pinimg.com/236x/66/eb/cb/66ebcb0b01921e17422650a3fb778954.jpg");
        userInfoPrefs.setFullname("Peter Pan");
    }

    private void setUpNavigationMenu() {
        //Navigation Menu
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        mRecyclerView.setHasFixedSize(true);

        mLeftMenuAdapter = new LeftMenuAdapter(getApplicationContext());

        mRecyclerView.setAdapter(mLeftMenuAdapter);

        final GestureDetector oneTouchGesture = new GestureDetector(MapsActivity.this,
                new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View view = rv.findChildViewUnder(e.getX(), e.getY());

                if (view != null && oneTouchGesture.onTouchEvent(e)) {
                    leftMenuDrawer.closeDrawers();
                    int clickedIndex = rv.getChildAdapterPosition(view);
                    if (clickedIndex == 0) {
                        //If the header was clicked
                        return false;
                    }

                    int index = clickedIndex - 1;
                    if (index == LeftMenuAdapter.ZONE_SETTING) {
                        goToSettings();
                    } else if (index == LeftMenuAdapter.EVENT_FILTER_SETTING) {
                        goToEventFilterSetting();
                    }
                    else if (index == LeftMenuAdapter.OBSERVATION_LIST) {
                        goToObsListActivity();
                    }

                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);


        leftMenuDrawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
    }

    public void goToSettings() {
        Intent intent = new Intent(this, ZoneSettingActivity.class);
        startActivityForResult(intent, 1);
    }

    public void goToEventFilterSetting() {
        Intent intent = new Intent(this, EventFilterActivity.class);
        startActivityForResult(intent, LeftMenuAdapter.EVENT_FILTER_SETTING);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        userDrawer.updateCurrentLocationInterestedArea();
        userDrawer.updateHomeLocation();

        updateAccidentsInRange();//Update accidents within the range after settings had changed
    }

    public void cancelServiceOnStop() {
//        stopUpdateCurrentLocationService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        updateGooglePlaces(); //Update health services
        updateAccidentsInRange();//Update accidents within the range
        userDrawer = new UserDrawer(getApplicationContext(), mMap);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

//        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        mMap.setInfoWindowAdapter(new CustomWindowInfoAdapter(getApplicationContext()));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (isLocationSwitcherShowed) {
                    switchLocationBar();
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                panToLatLng(marker.getPosition(), false);
                if (isLocationSwitcherShowed) {
                    switchLocationBar();
                }
                return true;
            }
        });

        updateAccidentsInRange();//Update accidents within the range after loading map
    }

    public void scheduleUpdateCurrentLocationService(){
        Intent intent = new Intent(getApplicationContext(), LocationAutoTracker.class);
        currentLocationPendingIntent = PendingIntent.
                getBroadcast(getApplicationContext(), LocationAutoTracker.id
                        , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        scheduleTask(currentLocationPendingIntent, updateCurrentLocationInterval);
    }

    public void scheduleUpdateAccidentList() {
        Intent intent = new Intent(getApplicationContext(), UpdateAccidentInRangeReceiver.class);
        updateAccidentListPendingIntent = PendingIntent.
                getBroadcast(getApplicationContext(), UpdateAccidentInRangeReceiver.id
                        , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        scheduleTask(updateAccidentListPendingIntent, updateAccidentListInterval);
    }

    public void scheduleTask(PendingIntent pendingIntent, long interval) {
        alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.
                setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                        interval, pendingIntent);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
    }

    public void stopUpdateCurrentLocationService() {
        alarmManager.cancel(currentLocationPendingIntent);
    }

    private BroadcastReceiver onAccidentListUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AccidentListParcelable parcelableExtra
                    = (AccidentListParcelable) intent
                    .getParcelableExtra(Constants.broadCastService_UpdateAccidentsList);

            accidentManager.setAccidentList(parcelableExtra.getAccidentList());
            accidentManager.updateAccidentMarkers(mMap);
        }
    };

    private BroadcastReceiver onCurrentLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(Constants.APPLICATION_ID, "Receive current location updated");

            userDrawer.updateCurrentLocationInterestedArea();//draw the radius

            updateGooglePlaces(); //Update health services

            updateAccidentsInRange();//Update accidents within the range

        }
    };

    public void toggleLeftMenu(View view) {
        leftMenuDrawer.openDrawer(Gravity.LEFT);
    }

    public void createNewReport(View view) {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

    public void switchLocationBar(View view) {
        switchLocationBar();
    }

    private void switchLocationBar() {
        int toolbarHeight = toolbar.getMeasuredHeight();
        int from = 0;
        int to = 0;
        if(isLocationSwitcherShowed) {
            //if visible
            from = toolbarHeight;//110;
            to = 0;
            isLocationSwitcherShowed = false;
        } else {
            //if invisible
            from = 0;
            to = 110;
            isLocationSwitcherShowed = true;
        }

        locationSwitcher.animate().translationY(to);
    }

    public void panToMyLocation() {
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        double lat = gpsTracker.getLatitude();
        double lon = gpsTracker.getLongitude();

        switchLocationText.setText(R.string.my_location);
        panToLatLng(new LatLng(lat, lon), true);

        GooglePlacesSearch placesSearch = new GooglePlacesSearch(getApplicationContext(), this);
        placesSearch.execute();
    }

    public void panToHomeLocation(){
        if(userSettingsPreferences.getHomeLocationAddress().isEmpty()){
            Toast.makeText(MapsActivity.this, R.string.home_location_not_set,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        switchLocationText.setText(R.string.home_location);
        double lat = userSettingsPreferences.getHomeLocationLat();
        double lon = userSettingsPreferences.getHomeLocationLon();

        panToLatLng(new LatLng(lat, lon), true);
    }

    public void panToLatLng(LatLng latLng, boolean zoom) {
        if (zoom) {
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(getCameraPosition(latLng)));
        } else {
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(getCameraPositionWithoutZoom(latLng)));
        }
    }
    public CameraPosition getCameraPosition(LatLng latLng) {
        return new CameraPosition.Builder()
                .target(latLng)
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
    }

    public CameraPosition getCameraPositionWithoutZoom(LatLng latLng) {
        return new CameraPosition.Builder()
                .target(latLng)
                .zoom(mMap.getCameraPosition().zoom)
                .build();
    }

    //Google Api clients callback methods
    @Override
    public void onConnected(Bundle bundle) {
        Log.e(Constants.APPLICATION_ID, "Google API client connected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(Constants.APPLICATION_ID, "Google API client suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(Constants.APPLICATION_ID, "Google API client failed");
    }

    protected void startLocationUpdates() {
        Intent intent = new Intent(getApplicationContext(), LocationAutoTracker.class);
        PendingIntent pIntent = PendingIntent.
                getBroadcast(getApplicationContext(), LocationAutoTracker.id
                        , intent, PendingIntent.FLAG_UPDATE_CURRENT);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, pIntent);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    //End google Api clients callback methods

    public void updateAccidentsInRange() {
        UpdateAccidentsInRange updateAccidentsInRange
                = new UpdateAccidentsInRange(getApplicationContext(), this);
        updateAccidentsInRange.execute();
    }

    @Override
    public void onAccidentsInRangeUpdated(AccidentList accidentList) {
        accidentManager.setAccidentList(accidentList);
        accidentManager.updateAccidentMarkers(mMap);
    }

    public void updateGooglePlaces() {
        GooglePlacesSearch googlePlacesSearch
                = new GooglePlacesSearch(getApplicationContext(), this);
        googlePlacesSearch.execute();
    }

    @Override
    public void onReceivedGooglePlacesSearch(PlacesList placesList) {
        healthServicesmanager.setPlacesListOfCurrentLocation(placesList);
        healthServicesmanager.updateHealthServicesMarkers(mMap);
	}
	
    public void updateMapFooter() {
        TextView showMyLocation = (TextView)findViewById(R.id.mylocation);
        TextView showMyRadius = (TextView) findViewById(R.id.myradius);
        TextView showRadiusEvents = (TextView) findViewById(R.id.myradiusevents);

        UserSettingsPreferences getradius = new UserSettingsPreferences(getApplicationContext());
        CurrentLocationPreferences currLocation = new CurrentLocationPreferences(getApplicationContext());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(currLocation.getLat(),
                    currLocation.getLon(), 1);
            if(addresses != null & addresses.size() > 0) {
                Address address = addresses.get(0);
                showMyLocation.setText(address.getLocality());
                showMyRadius.setText("(" + (getradius.getRadius()/1000) + " km)");
                //showRadiusEvents.setText( () + " events");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToObsListActivity() {
        Intent intent = new Intent(this, ObservationList.class);
        startActivityForResult(intent, requestCodeObsDetailed);
}

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("No", null).show();
    }


/*
found user, backend maager.
add some data

website or window form.
1st: maanger add data
thousands

2nd: can review all the data, the point of interest on  website
 */

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode,
                        Constants.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(Constants.APPLICATION_ID, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}