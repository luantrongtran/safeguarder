package ifn701.safeguarder.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.SharedPreferencesUtilities;

public class CurrentLocationPreferences {
    SharedPreferences sharedPreferences;

    public CurrentLocationPreferences(Context applicationContext) {
        sharedPreferences = applicationContext
                .getSharedPreferences(Constants.sharedPreferences_current_location,
                        Context.MODE_PRIVATE);
    }

    public void setLat(double lat) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferencesUtilities
                .putDoubleIntoSharedPreferences(editor,
                        Constants.sharedPreferences_current_location_lat, lat);

        editor.apply();
    }

    public double getLat() {
        return SharedPreferencesUtilities.getDoubleFromSharedPreferences(sharedPreferences,
                Constants.sharedPreferences_current_location_lat,
                Constants.sharedPreferences_float_default_value);
    }

    public void setLon(double lon) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferencesUtilities
                .putDoubleIntoSharedPreferences(editor,
                        Constants.sharedPreferences_current_location_lon, lon);

        editor.apply();
    }

    public double getLon() {
        return SharedPreferencesUtilities.getDoubleFromSharedPreferences(sharedPreferences,
                Constants.sharedPreferences_current_location_lon,
                Constants.sharedPreferences_float_default_value);
    }
}