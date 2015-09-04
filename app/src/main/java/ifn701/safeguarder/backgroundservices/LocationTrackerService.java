package ifn701.safeguarder.backgroundservices;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;

/**
 * This service will be called by LocationAutooTracker and stores current location into
 * SharedPreferences
 */
public class LocationTrackerService extends IntentService
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public GoogleApiClient mGoogleApiClient;
    private static int UPDATE_INTERVAL = 6*1000; // sec
    private static int FASTEST_INTERVAL = 5*1000; // sec
    private static int DISPLACEMENT = 10; // 10m

    public LocationTrackerService(){
        super("UpdateLocationService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("MyAPI", "update location service");
        buildGoogleApiClient();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("MyAPI", "Google api client connected 1");
        CurrentLocationPreferences currLocSharedPref
                = new CurrentLocationPreferences(getApplicationContext());
        Log.i(Constants.APPLIATION_ID, currLocSharedPref.getLat() + ", " + currLocSharedPref.getLon());

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(lastLocation == null) {
            Log.e(Constants.APPLIATION_ID, "Cannot get last location using FusedLocationApi");
        }

        Log.e("MyAPI", "My current location before storing: "
                + lastLocation.getLatitude() + ", " + lastLocation.getLongitude());


        currLocSharedPref.setLat(lastLocation.getLatitude());
        currLocSharedPref.setLon(lastLocation.getLongitude());

        Log.i(Constants.APPLIATION_ID, "after updating location");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void buildGoogleApiClient() {
        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
        if(mGoogleApiClient.isConnected() == false){
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("MyAPI", "Google api client failed to connect");
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest LocationRequest = null;
//        if(LocationRequest == null) {
        LocationRequest = new LocationRequest();
        LocationRequest.setInterval(UPDATE_INTERVAL);
        LocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationRequest.setSmallestDisplacement(DISPLACEMENT);
//        }

        return LocationRequest;
    }
}