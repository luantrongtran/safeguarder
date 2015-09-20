package ifn701.safeguarder.webservices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Vector;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;
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
                    = myApi.getAccidentInRange(currentLat, currentLon, radius).execute();
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
        iUpdateAccidentInRange.onUpdateAccidentsInRangeUpdated(accidentList);
    }
}