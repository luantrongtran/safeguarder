package ifn701.safeguarder.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

import ifn701.safeguarder.AccidentManager;
import ifn701.safeguarder.Constants;
import ifn701.safeguarder.activities.EventFilterActivity;

/**
 * Created by lua on 28/09/2015.
 * This class contains the settings of event filter.
 */
public class EventFilterSharedPreferences {
    SharedPreferences sharedPreferences;
    Context context;
    public EventFilterSharedPreferences(Context context) {
        this.context = context;
        sharedPreferences
                = context.getSharedPreferences(Constants.sharedPreferences_event_filter_settings,
                Context.MODE_PRIVATE);;
    }

    /**
     * @param key one of the key within the AccidentManager.sharedPreferencesIds
     * @param value
     */
    public void setEventFilter(String key, boolean value){
        sharedPreferences.edit().putBoolean(key.toLowerCase(), value).apply();
    }

    /**
     *
     * @param key one of the key within the AccidentManager.sharedPreferencesIds. Key is case-ignored
     * @return if the value is false, then all accident which have type is key will not be
     * displayed on the map.
     */
    public boolean getEventFilter(String key) {
        //if there is no setting for the given key, then, assuming the value is true
        return sharedPreferences.getBoolean(key.toLowerCase(), true);
    }

    public Map<String, Boolean> getSettings() {
        Map<String, Boolean> map = new HashMap<>();

        int size = AccidentManager.sharedPreferencesIds.length;
        for(int i = 0; i < size; i++) {
            boolean b = getEventFilter(AccidentManager.sharedPreferencesIds[i]);
            map.put(context.getString(AccidentManager.accidentTypeNames[i]), b);
        }

        return map;
    }
}
