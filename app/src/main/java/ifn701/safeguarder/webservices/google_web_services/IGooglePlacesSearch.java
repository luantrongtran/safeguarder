package ifn701.safeguarder.webservices.google_web_services;

import ifn701.safeguarder.entities.google_places.PlacesList;

/**
 * Created by lua on 15/09/2015.
 */
public interface IGooglePlacesSearch {
    public void onReceivedGooglePlacesSearch(PlacesList placesList);
}
