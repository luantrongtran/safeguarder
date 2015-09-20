package ifn701.safeguarder.backgroundservices;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.util.Vector;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;
import ifn701.safeguarder.Parcelable.AccidentListParcelable;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.backend.myApi.model.AccidentList;

public class UpdateAccidentsInRangeService extends IntentService {
    public static String ACTION = IntentService.class.getCanonicalName();

    public UpdateAccidentsInRangeService() {
        super("UpdateAccidentsInRangeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        UserSettingsPreferences userSettingsPref
                = new UserSettingsPreferences(getApplicationContext());
        float radius = userSettingsPref.getRadius();

        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(getApplicationContext());

        double currentLat = currentLocationPreferences.getLat();
        double currentLon = currentLocationPreferences.getLon();
        MyApi myApi = BackendApiProvider.getPatientApi();
        try {
            AccidentList accidentList
                    = myApi.getAccidentInRange(currentLat, currentLon, radius).execute();
            if(accidentList == null || accidentList.getAccidentList() == null){
                accidentList = new AccidentList();
                accidentList.setAccidentList(new Vector<Accident>());
            }

            Log.i(Constants.APPLICATION_ID, "update accident list service: "
                    + accidentList.getAccidentList().size() + " events");

            Intent in = new Intent(ACTION);
            AccidentListParcelable accidentListParcelable
                    = new AccidentListParcelable(accidentList);
            in.putExtra(Constants.broadCastService_UpdateAccidentsList, accidentListParcelable);
            LocalBroadcastManager.getInstance(this).sendBroadcast(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}