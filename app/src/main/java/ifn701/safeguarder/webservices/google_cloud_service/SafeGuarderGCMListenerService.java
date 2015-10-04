package ifn701.safeguarder.webservices.google_cloud_service;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.api.client.json.jackson2.JacksonFactory;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;
import ifn701.safeguarder.CustomSharedPreferences.NewAccidentWithinCurrentLocationSharedPreferences;
import ifn701.safeguarder.CustomSharedPreferences.NewAccidentWithinHomeLocationSharedPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;
import ifn701.safeguarder.backend.myApi.model.Accident;

/**
 * Created by lua on 24/09/2015.
 */
public class SafeGuarderGCMListenerService extends GcmListenerService {
    public static String TAG = "SafeGuarderGCMListenerService";
    public static final String ACTION_NEW_ACCIDENT = "new_accident_current_location";
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.i(TAG, "GCM msg received");
        String accidentTopic = "/topics/" + RegistrationIntentService.NEW_ACCIDENT_EVENT;
        if(from.equals(accidentTopic)) {
//            Toast.makeText(SafeGuarderGCMListenerService.this, "New accident", Toast.LENGTH_SHORT).show();
            Log.i(Constants.APPLICATION_ID, "New accident topic GCM");
            Accident accident = parseFromData(data);
            accident.setFactory(new JacksonFactory());
            Log.i(Constants.APPLICATION_ID, "GCM accident json string: " + accident.toString());
            if(checkAnAccidentWithinHomeLocation(accident)) {
                NewAccidentWithinHomeLocationSharedPreferences newAccident =
                        new NewAccidentWithinHomeLocationSharedPreferences(this);

                newAccident.putNewAccident(accident.getId()+"", accident.toString());
            } else if (checkAnAccidentWithinCurrentLocation(accident)) {
                NewAccidentWithinCurrentLocationSharedPreferences newAccident
                        = new NewAccidentWithinCurrentLocationSharedPreferences(this);

                newAccident.putNewAccident(accident.getId()+"", accident.toString());
            }

            Intent broadcastIntent = new Intent(ACTION_NEW_ACCIDENT);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        }
    }

    private Accident parseFromData(Bundle data) {
        Accident accident = new Accident();
        accident.setId(Integer.valueOf(data.getString("id")));
        accident.setUserId(Integer.valueOf(data.getString("userId")));
        accident.setName(data.getString("name"));
        accident.setObservationLevel(Integer.valueOf(data.getString("observation_level")));
        accident.setLat(Double.valueOf(data.getString("lat")));
        accident.setLon(Double.valueOf(data.getString("lon")));
        accident.setType(data.getString("type"));
        accident.setDescription(data.getString("description"));
        accident.setImage1(data.getString("image1"));
        accident.setImage2(data.getString("image2"));
        accident.setImage3(data.getString("image3"));
        accident.setTime(Long.valueOf(data.getString("time")));

        return accident;
    }

    public boolean checkAnAccidentWithinHomeLocation(Accident accident) {
        UserSettingsPreferences userSettingsPreferences = new UserSettingsPreferences(this);
        float radius = userSettingsPreferences.getRadius();
        double lat = userSettingsPreferences.getHomeLocationLat();
        double lon = userSettingsPreferences.getHomeLocationLon();

        float[] distance = new float[2];
        Location.distanceBetween(lat, lon, accident.getLat(), accident.getLon(), distance);

        return distance[0] < radius;
    }

    public boolean checkAnAccidentWithinCurrentLocation(Accident accident) {
        UserSettingsPreferences userSettingsPreferences = new UserSettingsPreferences(this);
        float radius = userSettingsPreferences.getRadius();
        CurrentLocationPreferences currentLocationPreferences = new CurrentLocationPreferences(this);
        double lat = currentLocationPreferences.getLat();
        double lon = currentLocationPreferences.getLon();

        float[] distance = new float[2];
        Location.distanceBetween(lat, lon, accident.getLat(), accident.getLon(), distance);

        return distance[0] < radius;
    }
}