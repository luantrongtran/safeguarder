package ifn701.safeguarder;

public class Constants {
    public static String webServiceUrl = "http://172.19.48.43:8080/_ah/api/" ;//"https://safeguarder-1097.appspot.com/_ah/api/";

    public static String APPLICATION_ID = "safeguarder";

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
    public static String getSharedPreferences_user_info_password ="password";

    public static String sharedPreferences_user_settings = "user_setting";
    public static String sharedPreferences_user_settings_radius = "radius";

    public static String sharedPreferences_user_settings_home_address = "home_address";
    public static String sharedPreferences_user_settings_home_lat = "home_lat";
    public static String sharedPreferences_user_settings_home_lon = "home_lon";

    public static String sharedPreferences_GCM = "google_cloud_message";
    public static String sharedPreferences_GCM_is_token_sent_to_server
            = "google_cloud_message_is_token_sent_to_server";

    public static String sharedPreferences_event_filter_settings = "event_filter_settings";
    public static String getSharedPreferences_event_filter_settings_by_time = "event_filter_settings"
           + "_by_time";

    public static String sharedPreferences_new_accident_current_location_notification
            = "new_accident_current_location";
    public static String sharedPreferences_new_accident_home_location_notification
            = "new_accident_home_location";

    public static String sharedPreferences_application_status = "application_status";

    public static String broadCastService_UpdateAccidentsList = "updated_accidents_list";

    public static String search_location_address = "search_location_address";
    public static String search_location_lat = "search_location_lat";
    public static String search_location_lon = "search_location_lon";

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static String notification_activity_intent_result_accident_lat = "accident_lat";
    public static String notification_activity_intent_result_accident_lon = "accident_lon";

    public static String start_from_intent_data = "start_from";//indicating from where the activity called.
    public static String start_from_notification = "start_from_notification";

    public static String observation_activity_intent_accident_id = "AccidentID";
}