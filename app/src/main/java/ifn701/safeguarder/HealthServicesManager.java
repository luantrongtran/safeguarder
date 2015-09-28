package ifn701.safeguarder;

import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ifn701.safeguarder.activities.CustomWindowInfoAdapter;
import ifn701.safeguarder.entities.google_places.Place;
import ifn701.safeguarder.entities.google_places.PlacesList;
import ifn701.safeguarder.webservices.MarkerIconUrlLoader;

public class HealthServicesManager {
    public PlacesList getPlacesListOfCurrentLocation() {
        return placesListOfCurrentLocation;
    }

    public void setPlacesListOfCurrentLocation(PlacesList placesListOfCurrentLocation) {
        this.placesListOfCurrentLocation = placesListOfCurrentLocation;
    }

    PlacesList placesListOfCurrentLocation;
    ArrayList<Marker> healthServicesMarkersOfCurrentLocation;

    public static Map<String, String> iconUrls = new HashMap<>();

    public HealthServicesManager() {
        healthServicesMarkersOfCurrentLocation = new ArrayList<>();

        placesListOfCurrentLocation = new PlacesList();
        placesListOfCurrentLocation.results = new ArrayList<>();
    }

    public void updateHealthServicesMarkers(GoogleMap gMap) {
        if(placesListOfCurrentLocation == null || placesListOfCurrentLocation.results == null) {
            return;
        }

        ArrayList<Marker> tempMarkers = new ArrayList<>();
        for(Place place : placesListOfCurrentLocation.results) {
            boolean b = false;
            for (Marker marker : healthServicesMarkersOfCurrentLocation) {
                if(marker.getSnippet().equals(place.place_id)) {
                    tempMarkers.add(marker);
                    b = true;
                    healthServicesMarkersOfCurrentLocation.remove(marker);
                    break;
                }
            }
            if(b){
                continue;
            }

            LatLng latLng = new LatLng(place.geometry.location.lat, place.geometry.location.lng);

            MarkerOptions markerOptions = CustomWindowInfoAdapter
                    .createMarkerOptions(CustomWindowInfoAdapter.GOOGLE_PLACE_TYPE);
            markerOptions = markerOptions.position(latLng).snippet("Snippet");
            Bitmap iconBitmap = ImageDownloaderManager.getAnImage(place.icon);
            if(iconBitmap != null){
                markerOptions = markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconBitmap));
            } else {
                MarkerIconUrlLoader markerIconUrlLoader = new MarkerIconUrlLoader();
                markerIconUrlLoader.execute(place.icon);
            }
            markerOptions.snippet(place.place_id);
            Marker marker = gMap.addMarker(markerOptions);

            tempMarkers.add(marker);
        }

        for (Marker marker : healthServicesMarkersOfCurrentLocation) {
            marker.remove();
        }

        healthServicesMarkersOfCurrentLocation = tempMarkers;
    }
}
