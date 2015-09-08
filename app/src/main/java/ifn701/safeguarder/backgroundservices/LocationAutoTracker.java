package ifn701.safeguarder.backgroundservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This class track the current location based on what scheduled in the MapsActivity
 */
public class LocationAutoTracker extends BroadcastReceiver {
    public static int id = 2;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyApp", "abc");

        Intent in = new Intent(context, LocationTrackerService.class);
        context.startService(in);
    }
}
