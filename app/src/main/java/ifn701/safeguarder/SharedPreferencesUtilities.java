package ifn701.safeguarder;

import android.content.SharedPreferences;

/**
 * References:
 * put and get double using SharedPreferences
 * http://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences
 *
 */
public class SharedPreferencesUtilities {
    public static void putDoubleIntoSharedPreferences(SharedPreferences.Editor editor, String key, double value) {
        editor.putLong(key, Double.doubleToRawLongBits(value)).apply();
    }

    public static double getDoubleFromSharedPreferences(SharedPreferences sharedPref, String key, double defaultValue) {
        if ( !sharedPref.contains(key)) {
            return defaultValue;
        }
        return Double.longBitsToDouble(sharedPref.getLong(key, 0));
    }
}