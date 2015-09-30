package ifn701.safeguarder.backend.entities;

import com.google.appengine.repackaged.org.codehaus.jackson.map.util.JSONPObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by lua on 1/10/2015.
 */
public class GCMSender {
    public static final String API_KEY = "";
    private static final String GCM_URL = "https://android.googleapis.com/gcm/send";

    public static void broadcastANewAccident(Accident accident) {


        try {
            URL url = new URL(GCM_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
        } catch( Exception ex) {

        }
    }
}
