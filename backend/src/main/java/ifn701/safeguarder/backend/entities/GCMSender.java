package ifn701.safeguarder.backend.entities;

import com.google.appengine.repackaged.org.codehaus.jackson.map.util.JSONPObject;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by lua on 1/10/2015.
 */
public class GCMSender {
    public static final String API_KEY = "AIzaSyAoQnICp2qr6u_5IBwnl9iIvyjewo6BqZ0";
    private static final String GCM_URL = "https://android.googleapis.com/gcm/send";

    /**
     * Broadcast a new accident to all user except for the sender who posted the accident.
     * @param accident
     */
    public static void broadcastANewAccident(Accident accident) {

        JSONObject jGcmData = new JSONObject();

        jGcmData.put("to", "/topics/newaccident");
        JSONObject jData = accident.toJSon();
        jGcmData.put("data", jData);
        try {
            URL url = new URL(GCM_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = IOUtils.toString(inputStream);
            System.out.println(resp);
            System.out.println("Check your device/emulator for notification or logcat for " +
                    "confirmation of the receipt of the GCM message.");
        } catch( Exception ex) {

        }
    }
}
