package ifn701.safeguarder.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn701.safeguarder.Constants;

public class UserInfoPreferences {
    private SharedPreferences sharedPreferences;

    public UserInfoPreferences(Context applicationContext) {
        sharedPreferences = applicationContext
                .getSharedPreferences(Constants.sharedPreferences_user_info, Context.MODE_PRIVATE);
    }

    public void setUserId(int userId) {
        sharedPreferences.edit().putInt(Constants.sharedPreferences_user_info_id, userId).apply();
    }

    public int getUserId() {
        return sharedPreferences
                .getInt(Constants.sharedPreferences_user_info_id,
                        Constants.sharedPreferences_integer_default_value);
    }

    public String getProfilePicture() {
        return sharedPreferences
                .getString(Constants.sharedPreferences_user_info_profile_picture,
                        Constants.sharedPreferences_string_default_value);
    }

    public void setProfilePicture(String url) {
        sharedPreferences.edit().putString(Constants.sharedPreferences_user_info_profile_picture,
                url).apply();
    }

    public String getFullname() {
        return sharedPreferences.getString(Constants.getSharedPreferences_user__info_fullname,
                Constants.sharedPreferences_string_default_value);
    }

    public void setFullname (String fullname) {
        sharedPreferences.edit().putString(Constants.getSharedPreferences_user__info_fullname,
                fullname).apply();
    }

    public String getEmail(){
        return sharedPreferences.getString(Constants.sharedPreferences_user_info_email,
                Constants.sharedPreferences_string_default_value);
    }

    public void setEmail (String email) {
        sharedPreferences.edit()
                .putString(Constants.sharedPreferences_user_info_email, email).apply();
    }
}
