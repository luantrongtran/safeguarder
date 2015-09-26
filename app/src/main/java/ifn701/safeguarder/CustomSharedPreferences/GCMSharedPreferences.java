package ifn701.safeguarder.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn701.safeguarder.Constants;


public class GCMSharedPreferences {
    SharedPreferences sharedPreferences;

    public GCMSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.sharedPreferences_GCM,
                Context.MODE_PRIVATE);
    }

    public void setIsTokenSentToServer(boolean isSent) {
        sharedPreferences.edit().putBoolean(Constants.sharedPreferences_GCM_is_token_sent_to_server,
                isSent).apply();
    }

    public boolean isTokenSentToServer() {
        return sharedPreferences.getBoolean(Constants.sharedPreferences_GCM_is_token_sent_to_server,
                false);
    }
}