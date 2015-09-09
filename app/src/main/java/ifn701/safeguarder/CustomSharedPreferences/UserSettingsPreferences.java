package ifn701.safeguarder.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.SharedPreferencesUtilities;

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
        sharedPreferences.edit().clear()
                .putFloat(Constants.sharedPreferences_user_settings_radius, radius)
                .commit();
    }

    public float getRadius() {
        return sharedPreferences
                .getFloat(Constants.sharedPreferences_user_settings_radius,
                        Constants.sharedPreferences_default_radius);
    }

    public void setHomeLocationLat(double lat) {
        SharedPreferencesUtilities.putDoubleIntoSharedPreferences(sharedPreferences.edit(),
                Constants.sharedPreferences_user_settings_home_lat, lat);
    }

    public double getHomeLocationLat() {
        return SharedPreferencesUtilities.getDoubleFromSharedPreferences(sharedPreferences,
                Constants.sharedPreferences_user_settings_home_lat,
                Constants.sharedPreferences_double_default_value);
    }

    public void setHomeLocationLon(double lon) {
        SharedPreferencesUtilities.putDoubleIntoSharedPreferences(sharedPreferences.edit(),
                Constants.sharedPreferences_user_settings_home_lon, lon);
    }

    public double getHomeLocationLon() {
        return SharedPreferencesUtilities.getDoubleFromSharedPreferences(sharedPreferences,
                Constants.sharedPreferences_user_settings_home_lon,
                Constants.sharedPreferences_double_default_value);
    }

    public void setHomeLocationAddress(String address) {
        sharedPreferences.edit().putString(Constants.sharedPreferences_user_settings_home_address,
                address).apply();
    }

    public String getHomeLocationAddress(){
        return sharedPreferences.getString(Constants.sharedPreferences_user_settings_home_address,
                Constants.sharedPreferences_string_default_value);
    }
}