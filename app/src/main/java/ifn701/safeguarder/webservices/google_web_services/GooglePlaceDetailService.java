package ifn701.safeguarder.webservices.google_web_services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.R;
import ifn701.safeguarder.entities.google_places.Place;
import ifn701.safeguarder.entities.google_places.PlaceDetail;

/**
 * Created by lua on 17/09/2015.
 */
public class GooglePlaceDetailService extends AsyncTask<String, Void, Place> {
    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/details/json?";// "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final JacksonFactory jacksonFactory = new JacksonFactory();
    private static final HttpTransport transport = new ApacheHttpTransport();

    private Context context;

    public GooglePlaceDetailService(Context context) {
        this.context = context;
    }

    /**
     *
     * @param params The first param must be Google place id
     * @return
     */
    @Override
    protected Place doInBackground(String... params) {
        try {
            HttpRequestFactory httpRequestFactory = RequestFactorCreator.createRequestFactory(transport, jacksonFactory);
            HttpRequest request = null;

            request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
            request.getUrl().put("key", context.getString(R.string.google_places_key));
            String placeId = params[0];
            request.getUrl().put("placeid", placeId);

            Log.i(Constants.APPLICATION_ID, request.getUrl().toString());
            PlaceDetail placeDetail  = request.execute().parseAs(PlaceDetail.class);
            return placeDetail.result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
