package ifn701.safeguarder;

/*
 * Created by lua on 16/08/2015.
 */
public class Constants {
    public static String webServiceUrl = "http://192.168.0.106:8080/_ah/api/";

    public static String APPLIATION_ID = "safeguarder";

    public static int sharedPreferences_integer_default_value = -1;
    public static float sharedPreferences_float_default_value = -1f;
    public static double sharedPreferences_double_default_value = -1d;
    public static long sharedPreferences_long_default_value = -1l;
    public static String sharedPreferences_string_default_value = "";

    public static float sharedPreferences_default_radius = 10f;

    public static String sharedPreferences_current_location = "current_location";
    public static String sharedPreferences_current_location_lat = "current_location_lat";
    public static String sharedPreferences_current_location_lon = "current_location_lon";

    public static String sharedPreferences_user_info = "user_info";
    public static String sharedPreferences_user_info_id = "user_id";
    public static String sharedPreferences_user_info_profile_picture = "profile_picture";
    public static String sharedPreferences_user_info_email = "user_email";
    public static String getSharedPreferences_user__info_fullname = "fullname";

    public static String sharedPreferences_user_settings = "user_setting";
    public static String sharedPreferences_user_settings_radius = "radius";

    public static String sharedPreferences_user_settings_home_address = "home_address";
    public static String sharedPreferences_user_settings_home_lat = "home_lat";
    public static String sharedPreferences_user_settings_home_lon = "home_lon";

    public static String broadCastService_UpdateAccidentsList = "updated_accidents_list";

    public static String search_location_address = "search_location_address";
    public static String search_location_lat = "search_location_lat";
    public static String search_location_lon = "search_location_lon";

}