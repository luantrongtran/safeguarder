package ifn701.safeguarder.backgroundservices;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;
import ifn701.safeguarder.CustomSharedPreferences.NewAccidentWithinCurrentLocationSharedPreferences;
import ifn701.safeguarder.CustomSharedPreferences.NewAccidentWithinHomeLocationSharedPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;
import ifn701.safeguarder.Parcelable.AccidentListParcelable;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.backend.myApi.model.AccidentList;

public class UpdateAccidentsInRangeService extends IntentService {
    public static String ACTION = IntentService.class.getCanonicalName();

    UserInfoPreferences userInfoPreferences;
    UserSettingsPreferences userSettingsPref;
    CurrentLocationPreferences currentLocationPreferences;

    public UpdateAccidentsInRangeService() {
        super("UpdateAccidentsInRangeService");

        userInfoPreferences = new UserInfoPreferences(this);
        userSettingsPref
                = new UserSettingsPreferences(getApplicationContext());
        currentLocationPreferences
                = new CurrentLocationPreferences(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(this);
//        UserSettingsPreferences userSettingsPref
//                = new UserSettingsPreferences(getApplicationContext());
        float radius = userSettingsPref.getRadius();

//        CurrentLocationPreferences currentLocationPreferences
//                = new CurrentLocationPreferences(getApplicationContext());

        double currentLat = currentLocationPreferences.getLat();
        double currentLon = currentLocationPreferences.getLon();
        MyApi myApi = BackendApiProvider.getPatientApi();
        try {
            AccidentList accidentList
                    = myApi.getAccidentInRange(userInfoPreferences.getUserId(), currentLat, currentLon, radius).execute();
            if(accidentList == null || accidentList.getAccidentList() == null){
                accidentList = new AccidentList();
                accidentList.setAccidentList(new Vector<Accident>());
            }

            //update notification list
            updateAccidentsInNotificationList(accidentList.getAccidentList());

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

    /**
     * update accidents in notification list, the notification list only keeps the accidents in
     * accidentList
     */
    public void updateAccidentsInNotificationList(List<Accident> accidentList) {
        NewAccidentWithinHomeLocationSharedPreferences newAccidentHomeLocation
                = new NewAccidentWithinHomeLocationSharedPreferences(this);
        NewAccidentWithinCurrentLocationSharedPreferences newAccidentCurrentLocation
                = new NewAccidentWithinCurrentLocationSharedPreferences(this);
        //Check notification list of home location first
        Set<String> keySet = newAccidentHomeLocation.getAll().keySet();
        Log.i(Constants.APPLICATION_ID, "List of home location: " + keySet.size()+"");
        for (String key : keySet) {
            String accidentId = "";
            for (Accident accident : accidentList) {
                accidentId = accident.getId()+"";
                if(accidentId.equals(key)) {
                    break;
                }
            }
            if(!accidentId.isEmpty()) {
                newAccidentHomeLocation.remove(accidentId);
            }
        }

        //Check notification list of current location
        keySet = newAccidentCurrentLocation.getAll().keySet();
        Log.i(Constants.APPLICATION_ID, "List of current location: " + keySet.size()+"");
        for (String key : keySet) {
            String accidentId = "";
            for (Accident accident : accidentList) {
                accidentId = accident.getId()+"";
                if(accidentId.equals(key)) {
                    break;
                }
            }
            if(!accidentId.isEmpty()) {
                newAccidentCurrentLocation.remove(accidentId);
            }
        }
    }
}