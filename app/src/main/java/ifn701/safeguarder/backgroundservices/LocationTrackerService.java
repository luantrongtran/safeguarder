package ifn701.safeguarder.backgroundservices;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * This service will be called by LocationAutooTracker and stores current location into
 * SharedPreferences
 */
public class LocationTrackerService extends IntentService {
    public static String ACTION = LocationTrackerService.class.getCanonicalName();

    public LocationTrackerService(){
        super("UpdateLocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
//        CurrentLocationPreferences curLocPrefs = new CurrentLocationPreferences(getApplication());
//
//        double lat = gpsTracker.getLatitude();
//        double lon = gpsTracker.getLongitude();
//        curLocPrefs.setLat(lat);
//        curLocPrefs.setLon(lon);

//        Log.e(Constants.APPLIATION_ID, "update location service: " + lat + ", " + lon);

        Intent in = new Intent(ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);
    }
}