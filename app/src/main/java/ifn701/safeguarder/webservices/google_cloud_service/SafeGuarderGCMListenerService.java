package ifn701.safeguarder.webservices.google_cloud_service;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

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
    public static final String ACTION_NEW_ACCIDENT_NEAR_CURRENT_LOCATION = "new_accident_current_location";
    public static final String ACTION_NEW_ACCIDENT_NEAR_HOME_LOCATION = "new_accident_home_location";
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.i(TAG, from);
        String message = data.toString();
        Log.i(TAG, message);

        String accidentTopic = "/topics/" + RegistrationIntentService.NEW_ACCIDENT_EVENT;
        if(from == accidentTopic) {
            Accident accident = parseFromData(data);
            if(checkAnAccidentWithinHomeLocation(accident)) {
                NewAccidentWithinHomeLocationSharedPreferences newAccident =
                        new NewAccidentWithinHomeLocationSharedPreferences(this);

                newAccident.putNewAccident(accident.getId()+"", accident.toString());
            } else if (checkAnAccidentWithinCurrentLocation(accident)) {
                NewAccidentWithinCurrentLocationSharedPreferences newAccident
                        = new NewAccidentWithinCurrentLocationSharedPreferences(this);

                newAccident.putNewAccident(accident.getId()+"", accident.toString());
            }
        }
    }

    private Accident parseFromData(Bundle data) {
        Accident accident = new Accident();
        accident.setId(data.getInt("id"));
        accident.setUserId(data.getInt("userId"));
        accident.setName(data.getString("name"));
        accident.setObservationLevel(data.getInt("observation_level"));
        accident.setLat(data.getDouble("lat"));
        accident.setLon(data.getDouble("lon"));

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