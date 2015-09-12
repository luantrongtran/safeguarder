/**
 * References https://github.com/ddewaele/GooglePlacesApi
 */
package ifn701.safeguarder.webservices;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;;import java.io.IOException;

import ifn701.safeguarder.GPSTracker;
import ifn701.safeguarder.R;
import ifn701.safeguarder.entities.Place;
import ifn701.safeguarder.entities.PlacesList;

public class GooglePlacesSearch extends AsyncTask<String, Void, PlacesList> {
    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final JacksonFactory jacksonFactory = new JacksonFactory();
    private boolean PRINT_AS_STRING = false;
    private static final HttpTransport transport = new ApacheHttpTransport();


    Context context;

    public GooglePlacesSearch (Context context) {
        this.context = context;
    }

    @Override
    protected PlacesList doInBackground(String... params) {
        try {
            System.out.println("Perform Search ....");
            System.out.println("-------------------");
            HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
            HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));


            request.getUrl().put("key", context.getString(R.string.google_places_key));
            GPSTracker gpsTracker = new GPSTracker(context);
            request.getUrl().put("location", gpsTracker.getLatitude() + ","
                    + gpsTracker.getLongitude());
            request.getUrl().put("radius", 500);
            request.getUrl().put("sensor", "false");

            PlacesList places = null;
            if (PRINT_AS_STRING) {
                System.out.println(request.execute().parseAsString());
            } else {

                places = request.execute().parseAs(PlacesList.class);
                System.out.println("STATUS = " + places.status);
                for (Place place : places.results) {
                    System.out.println(place);
                }
            }

            return places;
        } catch (HttpResponseException e) {
            System.err.println(e.getStatusMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpRequestFactory createRequestFactory(final HttpTransport transport) {
        return transport.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                JsonObjectParser parser = new JsonObjectParser(jacksonFactory);
                request.setParser(parser);
            }
        });
    }
}
