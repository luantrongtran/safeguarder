package ifn701.safeguarder.webservices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.NewAccidentWithinCurrentLocationSharedPreferences;
import ifn701.safeguarder.CustomSharedPreferences.NewAccidentWithinHomeLocationSharedPreferences;
import ifn701.safeguarder.backend.myApi.model.Accident;

/**
 * Created by lua on 4/10/2015.
 * This gets the list of accidents in notification list
 */
public class GetNotificationListService extends AsyncTask<Void, Void, List<Accident>> {

    private IGetNotificationListService iGetNotificationListService;
    NewAccidentWithinHomeLocationSharedPreferences newAccidentHomeLocation;
    NewAccidentWithinCurrentLocationSharedPreferences newAccidentCurrentLocation;
    List<Accident> accidentList;
    Context context;
    public GetNotificationListService(Context context,
                                      IGetNotificationListService iGetNotificationListService) {
        this.context = context;
        this.iGetNotificationListService = iGetNotificationListService;

        newAccidentCurrentLocation = new NewAccidentWithinCurrentLocationSharedPreferences(context);
        newAccidentHomeLocation = new NewAccidentWithinHomeLocationSharedPreferences(context);
    }

    @Override
    protected List<Accident> doInBackground(Void... params) {
        accidentList = new ArrayList<>();

        getAccidentsWithinCurrentLocation();
        getAccidentsWithinHomeLocation();

        return accidentList;
    }

    private void getAccidentsWithinHomeLocation() {
        Map<String, ?> map = newAccidentHomeLocation.getAll();
        if(map == null) {
            return;
        }

        List<String> values
                = new ArrayList<>((Collection<? extends String>) map.values());

        JacksonFactory jacksonFactory = new JacksonFactory();

        for(String json : values) {
            Accident accident;
            Log.i(Constants.APPLICATION_ID, json);
            try {
                accident = jacksonFactory.createJsonParser(json).parse(Accident.class);
                accidentList.add(accident);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void getAccidentsWithinCurrentLocation() {
        Map<String, ?> map = newAccidentCurrentLocation.getAll();
        if(map == null) {
            return;
        }

        List<String> values
                = new ArrayList<>((Collection<? extends String>) map.values());

        JacksonFactory jacksonFactory = new JacksonFactory();

        for(String json : values) {
            Accident accident;
            Log.i(Constants.APPLICATION_ID, json);
            try {
                accident = jacksonFactory.createJsonParser(json).parse(Accident.class);
                accidentList.add(accident);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onPostExecute(List<Accident> accidentList) {
        iGetNotificationListService.onNotificationListUpdated(accidentList);
    }
}
