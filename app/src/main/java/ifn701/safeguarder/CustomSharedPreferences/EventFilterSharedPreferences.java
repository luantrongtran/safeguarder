package ifn701.safeguarder.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.activities.EventFilterActivity;

/**
 * Created by lua on 28/09/2015.
 * This class contains the settings of event filter.
 */
public class EventFilterSharedPreferences {
    SharedPreferences sharedPreferences;
    public EventFilterSharedPreferences(Context context) {
        sharedPreferences
                = context.getSharedPreferences(Constants.sharedPreferences_event_filter_settings,
                Context.MODE_PRIVATE);;
    }

    /**
     * @param key one of the key within the AccidentManager.sharedPreferencesIds
     * @param value
     */
    public void setEventFilter(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     *
     * @param key one of the key within the AccidentManager.sharedPreferencesIds
     * @return if the value is false, then all accident which have type is key will not be
     * displayed on the map.
     */
    public boolean getEventFilter(String key) {
        //if there is no setting for the given key, then, assuming the value is true
        return sharedPreferences.getBoolean(key, true);
    }
}
