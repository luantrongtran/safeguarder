package ifn701.safeguarder.backgroundservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.GPSTracker;
import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;

/**
 * This class track the current location based on what scheduled in the MapsActivity
 */
public class LocationAutoTracker extends BroadcastReceiver {
    public static int id = 2;
    @Override
    public void onReceive(Context context, Intent intent) {
        GPSTracker gpsTracker = new GPSTracker(context);
        CurrentLocationPreferences cur = new CurrentLocationPreferences(context);
        cur.setLat(gpsTracker.getLatitude());
        cur.setLon(gpsTracker.getLongitude());
        Log.e(Constants.APPLICATION_ID, "Auto tracker receiver");

        Intent in = new Intent(context, LocationTrackerService.class);
        context.startService(in);
    }
}