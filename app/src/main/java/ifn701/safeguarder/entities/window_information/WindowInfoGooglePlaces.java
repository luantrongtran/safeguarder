package ifn701.safeguarder.entities.window_information;

import com.google.api.client.util.Key;

import ifn701.safeguarder.entities.google_places.Place;

/**
 * Created by lua on 17/09/2015.
 */
public class WindowInfoGooglePlaces {
    @Key
    private String type = WindowInfoGooglePlaces.class.getCanonicalName();

    @Key
    Place googlePlaceInformation;
}
