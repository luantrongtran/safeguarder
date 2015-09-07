package ifn701.safeguarder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;
import ifn701.safeguarder.Parcelable.AccidentListParcelable;
import ifn701.safeguarder.backgroundservices.LocationAutoTracker;
import ifn701.safeguarder.backgroundservices.UpdateAccidentInRangeReceiver;
import ifn701.safeguarder.backgroundservices.UpdateAccidentsInRangeService;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public long updateCurrentLocationInterval = 10*1000;//10s
    private AlarmManager alarmManager;
    private PendingIntent currentLocationPendingIntent;

    public long updateAccidentListInterval = 10*1000;//10s
    private PendingIntent updateAccidentListPendingIntent;

    public static AccidentManager accidentManager = new AccidentManager();
    public static UserDrawer userDrawer;

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
        registerAccidentListUpdateReceiver();
    }

    public void registerAccidentListUpdateReceiver() {
        IntentFilter onAccidentListUpdatedFilter
                = new IntentFilter(UpdateAccidentsInRangeService.ACTION);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(onAccidentListUpdated, onAccidentListUpdatedFilter);
    }

    public void setUpDummyData() {
        //Setup dummy data for logging in information.
        SharedPreferences userInfoPref = getApplicationContext().
                getSharedPreferences(Constants.sharedPreferences_user_info, MODE_PRIVATE);

        UserInfoPreferences userInfoPrefs = new UserInfoPreferences(getApplicationContext());
        userInfoPrefs.setUserId(1);

        UserSettingsPreferences userSettingsPreferences
                = new UserSettingsPreferences(getApplicationContext());
        userSettingsPreferences.setRadius(200);
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
                getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        scheduleTask(currentLocationPendingIntent, updateCurrentLocationInterval);
    }

    public void scheduleUpdateAccidentList() {
        Intent intent = new Intent(getApplicationContext(), UpdateAccidentInRangeReceiver.class);
        updateAccidentListPendingIntent = PendingIntent.
                getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        scheduleTask(updateAccidentListPendingIntent, updateAccidentListInterval);
    }

    public void scheduleTask(PendingIntent pendingIntent, long interval) {
        alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.
                setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                        interval, pendingIntent);
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
}