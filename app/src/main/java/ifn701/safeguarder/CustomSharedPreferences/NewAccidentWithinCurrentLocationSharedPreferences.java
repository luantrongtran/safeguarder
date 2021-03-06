package ifn701.safeguarder.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import ifn701.safeguarder.Constants;

/**
 * Created by lua on 2/10/2015.
 */
public class NewAccidentWithinCurrentLocationSharedPreferences {
    SharedPreferences sharedPreferences;

    public NewAccidentWithinCurrentLocationSharedPreferences(Context context) {
        sharedPreferences
                = context.getSharedPreferences(Constants
                .sharedPreferences_new_accident_current_location_notification, Context.MODE_PRIVATE);
    }

    public void putNewAccident(String key, String accidentJSonString) {
        sharedPreferences.edit().putString(key, accidentJSonString).apply();
    }

    /**
     *
     * @param key
     * @return JSon string of an accident, null if couldn't find the given key
     */
    public String getAccident(String key) {
        return sharedPreferences.getString(key, null);
    }

    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    public int size() {
        Map map = sharedPreferences.getAll();
        if(map != null) {
            return map.size();
        }
        return 0;
    }

    public boolean contains(int accidentId) {
        return sharedPreferences.contains(accidentId+"");
    }

    public void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
