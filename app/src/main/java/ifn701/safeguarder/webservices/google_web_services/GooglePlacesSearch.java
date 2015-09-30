/**
 * References https://github.com/ddewaele/GooglePlacesApi
 */
package ifn701.safeguarder.webservices.google_web_services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;
import ifn701.safeguarder.R;
import ifn701.safeguarder.entities.google_places.Place;
import ifn701.safeguarder.entities.google_places.PlacesList;

public class GooglePlacesSearch extends AsyncTask<Void, Void, PlacesList> {
    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";// "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final JacksonFactory jacksonFactory = new JacksonFactory();
    private static final HttpTransport transport = new ApacheHttpTransport();

    IGooglePlacesSearch interfaceGooglePlacesSearch;


    Context context;

    public GooglePlacesSearch (Context context, IGooglePlacesSearch interfaceGooglePlacesSearch) {
        this.context = context;
        this.interfaceGooglePlacesSearch = interfaceGooglePlacesSearch;
    }

    @Override
    protected PlacesList doInBackground(Void... params) {
        PlacesList list = null;
        try {
            CurrentLocationPreferences curLocPrefs = new CurrentLocationPreferences(context);
            UserSettingsPreferences userSettingsPreferences = new UserSettingsPreferences(context);
            float radius = userSettingsPreferences.getRadius();

            HttpRequestFactory httpRequestFactory = RequestFactorCreator.createRequestFactory(transport, jacksonFactory);
            HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));

            request.getUrl().put("key", context.getString(R.string.google_places_key));
            request.getUrl().put("location", curLocPrefs.getLat() + ","
                    + curLocPrefs.getLon());
            request.getUrl().put("radius", radius);
            request.getUrl().put("types", "hospital|dentist|police|health");

            list = request.execute().parseAs(PlacesList.class);
            System.out.println("STATUS = " + list.status);
            Log.i(Constants.APPLICATION_ID, request.getUrl().toString());
            for (Place place : list.results) {
                System.out.println(place);
            }

//            if (list.next_page_token != null || list.next_page_token != "") {
//                Thread.sleep(4000);
//                         /*Since the token can be used after a short time it has been  generated*/
//                request.getUrl().put("pagetoken", list.next_page_token);
//                PlacesList temp = request.execute().parseAs(PlacesList.class);
//                list.results.addAll(temp.results);
//
//                if (temp.next_page_token != null || temp.next_page_token != "") {
//                    Thread.sleep(4000);
//                    request.getUrl().put("pagetoken", temp.next_page_token);
//                    PlacesList tempList = request.execute().parseAs(PlacesList.class);
//                    list.results.addAll(tempList.results);
//                }
//            }

            return list;
        } catch (HttpResponseException e) {
            System.err.println(e.getStatusMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return list;
    }

    @Override
    protected void onPostExecute(PlacesList placesList) {
        if(placesList == null) {
            return;
        }
        interfaceGooglePlacesSearch.onReceivedGooglePlacesSearch(placesList);
    }
}
