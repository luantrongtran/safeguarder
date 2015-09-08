package ifn701.safeguarder.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn701.safeguarder.Constants;

/**
 * Created by lua on 3/09/2015.
 */
public class UserSettingsPreferences {
    SharedPreferences sharedPreferences;
    public UserSettingsPreferences(Context applicationContext) {
        sharedPreferences = applicationContext
                .getSharedPreferences(Constants.sharedPreferences_user_settings, Context.MODE_PRIVATE);
    }

    public void setRadius(float radius) {
        sharedPreferences.edit()
                .putFloat(Constants.sharedPreferences_user_settings_radius, radius)
                .apply();
    }

    public float getRadius() {
        return sharedPreferences
                .getFloat(Constants.sharedPreferences_user_settings_radius,
                        Constants.sharedPreferences_default_radius);
    }
}
