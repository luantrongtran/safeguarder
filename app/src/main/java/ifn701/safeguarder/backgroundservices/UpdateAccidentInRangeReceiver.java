package ifn701.safeguarder.backgroundservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lua on 3/09/2015.
 */
public class UpdateAccidentInRangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent updateAccidentInRangeServiceIntent
                = new Intent(context, UpdateAccidentsInRangeService.class);
        context.startService(updateAccidentInRangeServiceIntent);
    }
}
