package ifn701.safeguarder.webservices.google_cloud_service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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
import ifn701.safeguarder.MapsActivity;
import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.model.Accident;

/**
 * Created by lua on 24/09/2015.
 */
public class SafeGuarderGCMListenerService extends GcmListenerService {
    public static String TAG = "SafeGuarderGCMListenerService";
    public static final String ACTION_NEW_ACCIDENT = "new_accident_current_location";

    public static final int NEW_ACCIDENT_NOTIFICATION_HOME_LOCATION_ID = 1;
    public static final int NEW_ACCIDENT_NOTIFICATION_CURRENT_LOCATION_ID = 2;
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
                pushNotificationNewAccidentHomeLocation();//push notification
            } else if (checkAnAccidentWithinCurrentLocation(accident)) {
                NewAccidentWithinCurrentLocationSharedPreferences newAccident
                        = new NewAccidentWithinCurrentLocationSharedPreferences(this);

                newAccident.putNewAccident(accident.getId()+"", accident.toString());
                pushNotificationNewAccidentCurrentLocation();//push notification
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

    private void pushNotificationNewAccidentHomeLocation() {
        NewAccidentWithinHomeLocationSharedPreferences newAccident =
                new NewAccidentWithinHomeLocationSharedPreferences(this);
        String title = "New events";
        String content = newAccident.size() + " new event(s) near your home";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setPriority(2)
                        .setSmallIcon(R.drawable.home_icon)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MapsActivity.class);
        resultIntent.putExtra(Constants.start_from_intent_data, Constants.start_from_notification);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NEW_ACCIDENT_NOTIFICATION_HOME_LOCATION_ID, mBuilder.build());
    }

    private void pushNotificationNewAccidentCurrentLocation() {
        NewAccidentWithinCurrentLocationSharedPreferences newAccident
                = new NewAccidentWithinCurrentLocationSharedPreferences(this);
        String title = "New events";
        String content = newAccident.size() + " new event(s) near your place";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setPriority(2)
                        .setSmallIcon(R.drawable.ic_my_location_black_24dp)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setDefaults(Notification.DEFAULT_SOUND);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MapsActivity.class);
        resultIntent.putExtra(Constants.start_from_intent_data, Constants.start_from_notification);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NEW_ACCIDENT_NOTIFICATION_CURRENT_LOCATION_ID, mBuilder.build());
    }
}