package ifn701.safeguarder.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import ifn701.safeguarder.Constants;

/**
 * Created by lua on 2/10/2015.
 */
public class NewAccidentWithinHomeLocationSharedPreferences {
    SharedPreferences sharedPreferences ;

    public NewAccidentWithinHomeLocationSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants
                .sharedPreferences_new_accident_home_location_notification, Context.MODE_PRIVATE);
    }

    public void putNewAccident(String key, String bundle) {
        sharedPreferences.edit().putString(key, bundle).apply();
    }

    public String getAnAccident(String key) {
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
