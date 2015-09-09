package ifn701.safeguarder;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;
import ifn701.safeguarder.Parcelable.AccidentListParcelable;
import ifn701.safeguarder.activities.LeftMenuAdapter;
import ifn701.safeguarder.activities.ZoneSettingActivity;
import ifn701.safeguarder.backgroundservices.LocationAutoTracker;
import ifn701.safeguarder.backgroundservices.LocationTrackerService;
import ifn701.safeguarder.backgroundservices.UpdateAccidentInRangeReceiver;
import ifn701.safeguarder.backgroundservices.UpdateAccidentsInRangeService;


public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public long updateCurrentLocationInterval = 13*1000;//10s
    private AlarmManager alarmManager;
    private PendingIntent currentLocationPendingIntent;

    public long updateAccidentListInterval = 10*1000;//10s
    private PendingIntent updateAccidentListPendingIntent;

    public static AccidentManager accidentManager = new AccidentManager();
    public static UserDrawer userDrawer;

    //Navigation menu
    private Toolbar toolbar;

    RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mLeftMenuAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout drawer;

//    ActionBarDrawerToggle mDrawerToggle;
    //end navigation menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // mapFragment.getMapAsync(this);

        setUpDummyData();

        scheduleAutoService();
        registerReceiver();

        setUpNavigationMenu();
    }

    @Override
    protected void onStop() {
        cancelServiceOnStop();
        super.onStop();
    }

    /**
     * Register all necessary receivers
     */
    public void registerReceiver() {
//        registerAccidentListUpdateReceiver();
        registerCurrentLocationUpdateReceiver();
    }

    public void registerAccidentListUpdateReceiver() {
        IntentFilter onAccidentListUpdatedFilter
                = new IntentFilter(UpdateAccidentsInRangeService.ACTION);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(onAccidentListUpdated, onAccidentListUpdatedFilter);
    }

    public void registerCurrentLocationUpdateReceiver() {
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

        UserSettingsPreferences userSettingsPreferences
                = new UserSettingsPreferences(getApplicationContext());
        userSettingsPreferences.setRadius(200);
    }

    private void setUpNavigationMenu() {
        //Navigation Menu
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);

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
                    drawer.closeDrawers();
                    int clickedIndex = rv.getChildAdapterPosition(view);
                    if (clickedIndex == 0) {
                        //If the header was clicked
                        return false;
                    }

                    int index = clickedIndex - 1;
                    if (index == LeftMenuAdapter.ZONE_SETTING) {
                        goToSettings();
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


        drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
    }

    public void goToSettings() {
        Intent intent = new Intent(this, ZoneSettingActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        userDrawer.updateCurrentLocationInterestedArea();
    }

    public void scheduleAutoService() {
        scheduleUpdateCurrentLocationService();
        scheduleUpdateAccidentList();
    }

    public void cancelServiceOnStop() {
//        stopUpdateCurrentLocationService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

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
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").snippet("Snippet"));

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

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));
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
            Log.i(Constants.APPLIATION_ID, "Receive current location updated");
            userDrawer.updateCurrentLocationInterestedArea();
        }
    };

    public void toggleLeftMenu(View view) {
        drawer.openDrawer(Gravity.LEFT);
    }
}