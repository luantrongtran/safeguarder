package ifn701.safeguarder.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.SharedPreferencesUtilities;
import ifn701.safeguarder.backend.myApi.model.UserSetting;

/**
 * Created by lua on 3/09/2015.
 */
public class UserSettingsPreferences {
    SharedPreferences sharedPreferences;
    private Context context;
    public UserSettingsPreferences(Context applicationContext) {
        sharedPreferences = applicationContext
                .getSharedPreferences(Constants.sharedPreferences_user_settings, Context.MODE_PRIVATE);
        this.context = applicationContext;
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

    /**
     * put all the attributes of userSettings into SharedPreferences
     * @param userSettings
     */
    public void setUserSettings(UserSetting userSettings) {
        setHomeLocationAddress(userSettings.getHomeAddress());
        setHomeLocationLat(userSettings.getHomeLocationLat());
        setHomeLocationLon(userSettings.getHomeLocationLon());
        setRadius(userSettings.getRadius());
    }

    /**
     * get All the user settings from SharedPreferences and put them into an UserSetting object
     * @return an UserSetting object containing all data
     */
    public UserSetting getUserSettings() {
        UserSetting userSetting = new UserSetting();

        userSetting.setRadius(getRadius());
        userSetting.setHomeAddress(getHomeLocationAddress());
        userSetting.setHomeLocationLat(getHomeLocationLat());
        userSetting.setHomeLocationLon(getHomeLocationLon());

        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);
        userSetting.setUserId(userInfoPreferences.getUserId());

        return userSetting;
    }
}