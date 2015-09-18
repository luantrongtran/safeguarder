package ifn701.safeguarder.webservices.google_web_services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.R;

/**
 * Created by lua on 16/09/2015.
 */
public class GoogleImageService extends AsyncTask<String, Void, Bitmap> {
    private static final String IMAGE_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/photo?";
    private static final JacksonFactory jacksonFactory = new JacksonFactory();
    private static final HttpTransport transport = new ApacheHttpTransport();

    Context context;

    public GoogleImageService(Context context) {
        this.context = context;
    }

    /**
     *
     * @param params The first parameter should be the photo reference
     * @return
     */
    @Override
    protected Bitmap doInBackground(String... params) {
        String photoRef = params[0];
        if(photoRef == null || photoRef.isEmpty()) {
            return null;
        }

        HttpRequestFactory httpRequestFactory = RequestFactorCreator.createRequestFactory(transport, jacksonFactory);
        HttpRequest request = null;
        try {
            request = httpRequestFactory.buildGetRequest(new GenericUrl(IMAGE_SEARCH_URL));
            request.getUrl().put("key", context.getString(R.string.google_places_key));
            request.getUrl().put("photoreference", photoRef);
            request.getUrl().put("maxwidth", "400");

            Log.i(Constants.APPLIATION_ID, request.getUrl().toString());
            HttpResponse response = request.execute();

            if (response != null) {
                InputStream instream = response.getContent();
                Bitmap bm = convertInputstreamToBitmap(instream);
                return bm;
            }
//            BitmapDescriptorFactory.from
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Bitmap convertInputstreamToBitmap(InputStream instream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        try {
            // instream is content got from httpentity.getContent()
            while ((len = instream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = baos.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bmp;
    }
}