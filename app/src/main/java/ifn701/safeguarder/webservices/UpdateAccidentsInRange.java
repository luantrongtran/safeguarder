package ifn701.safeguarder.webservices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;
import ifn701.safeguarder.CustomSharedPreferences.NewAccidentWithinCurrentLocationSharedPreferences;
import ifn701.safeguarder.CustomSharedPreferences.NewAccidentWithinHomeLocationSharedPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.backend.myApi.model.AccidentList;

public class UpdateAccidentsInRange extends AsyncTask<Void, Void, AccidentList> {
    private Context context;
    IUpdateAccidentInRange iUpdateAccidentInRange;

    public UpdateAccidentsInRange(Context context, IUpdateAccidentInRange interfaceUpdate) {
        this.context = context;
        this.iUpdateAccidentInRange = interfaceUpdate;
    }

    @Override
    protected AccidentList doInBackground(Void... params) {
        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);

        UserSettingsPreferences userSettingsPref
                = new UserSettingsPreferences(context);
        float radius = userSettingsPref.getRadius();

        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(context);

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

            Log.i(Constants.APPLICATION_ID, "update accident list service: "
                    + accidentList.getAccidentList().size() + " events");

            return accidentList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(AccidentList accidentList) {
        if(accidentList == null) {
            return;
        }
        updateAccidentsInNotificationList(accidentList.getAccidentList());
        iUpdateAccidentInRange.onAccidentsInRangeUpdated(accidentList);
    }

    /**
     * update accidents in notification list, the notification list only keeps the accidents in
     * accidentList
     */
    public void updateAccidentsInNotificationList(List<Accident> accidentList) {
        NewAccidentWithinHomeLocationSharedPreferences newAccidentHomeLocation
                = new NewAccidentWithinHomeLocationSharedPreferences(context);
        NewAccidentWithinCurrentLocationSharedPreferences newAccidentCurrentLocation
                = new NewAccidentWithinCurrentLocationSharedPreferences(context);
        //Check notification list of home location first
        Set<String> keySet = newAccidentHomeLocation.getAll().keySet();
        Log.i(Constants.APPLICATION_ID, "List of home location: " + keySet.size()+"");
        for (String key : keySet) {
            boolean b = false;
            for (Accident accident : accidentList) {
                String temp = accident.getId()+"";
                if(temp.equals(key)) {
                    b = true;
                    break;
                }
            }
            if(!b) {
                newAccidentHomeLocation.remove(key);
            }
        }

        //Check notification list of current location
        keySet = newAccidentCurrentLocation.getAll().keySet();
        Log.i(Constants.APPLICATION_ID, "List of current location: " + keySet.size() + "");
        for (String key : keySet) {
            boolean b = false;
            for (Accident accident : accidentList) {
                String temp = accident.getId()+"";
                if(temp.equals(key)) {
                    b = true;
                    break;
                }
            }
            if(!b) {
                newAccidentCurrentLocation.remove(key);
            }
        }
    }
}