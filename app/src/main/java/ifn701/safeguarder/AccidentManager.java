package ifn701.safeguarder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Vector;

import ifn701.safeguarder.activities.CustomWindowInfoAdapter;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.backend.myApi.model.AccidentList;
import ifn701.safeguarder.entities.window_information.WindowInfoGooglePlaces;

/**
 * Created by lua on 7/09/2015.
 */
public class AccidentManager {
    AccidentList accidentList;

    Vector<Marker> accidentMarkers;

    public AccidentManager() {
        accidentList = new AccidentList();
        accidentMarkers = new Vector<>();
    }

    public AccidentList getAccidentList() {
        return accidentList;
    }

    public void setAccidentList(AccidentList accidentList) {
        this.accidentList = accidentList;
    }

    public void updateAccidentMarkers(GoogleMap gMap) {
        if(accidentList == null || accidentList.getAccidentList() == null){
            return;
        }

        for(Marker marker : accidentMarkers){
            marker.remove();
        }
        accidentMarkers.clear();

        MarkerOptions markerOptions = CustomWindowInfoAdapter
                .createMarkerOptions(CustomWindowInfoAdapter.ACCIDENT_TYPE);
        List<Accident> accidents = accidentList.getAccidentList();
        for(int i = 0; i < accidents.size(); i++) {
            Accident accident = accidents.get(i);
            LatLng position = new LatLng(accident.getLat(), accident.getLon());
            markerOptions.snippet(accident.toString());
            Marker marker = gMap.addMarker(markerOptions.position(position));
            accidentMarkers.add(marker);
        }
    }
}
