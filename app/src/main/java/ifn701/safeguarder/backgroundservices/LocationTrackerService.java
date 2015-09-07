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
import ifn701.safeguarder.GPSTracker;

/**
 * This service will be called by LocationAutooTracker and stores current location into
 * SharedPreferences
 */
public class LocationTrackerService extends IntentService {

    public GoogleApiClient mGoogleApiClient;
    private static int UPDATE_INTERVAL = 6*1000; // sec
    private static int FASTEST_INTERVAL = 5*1000; // sec
    private static int DISPLACEMENT = 10; // 10m

    public LocationTrackerService(){
        super("UpdateLocationService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        CurrentLocationPreferences curLocPrefs = new CurrentLocationPreferences(getApplication());

        double lat = gpsTracker.getLatitude();
        double lon = gpsTracker.getLongitude();
        curLocPrefs.setLat(lat);
        curLocPrefs.setLon(lon);

        Log.e(Constants.APPLIATION_ID, "update location service: " + lat + ", " + lon);
    }
}