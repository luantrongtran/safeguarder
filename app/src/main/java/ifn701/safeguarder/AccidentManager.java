package ifn701.safeguarder;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ifn701.safeguarder.activities.CustomWindowInfoAdapter;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.backend.myApi.model.AccidentList;
import ifn701.safeguarder.entities.window_information.WindowInfoGooglePlaces;

public class AccidentManager {
    AccidentList accidentList;

    Vector<Marker> accidentMarkers;
    Context context;

    private Map<String, BitmapDescriptor> accidentTypeMarkers;
    //accidentTypeMarkerIds should be corresponding to the accidentType_array in string.xml
    private static int[] accidentTypeMarkerIds = {R.drawable.aviation_marker, R.drawable.crime_marker,
            R.drawable.earthquake_marker, R.drawable.ferry_marker, R.drawable.industry_accident_marker,
            R.drawable.traffic_accident_marker, R.drawable.weather_marker};

    public AccidentManager(Context context) {
        accidentList = new AccidentList();
        accidentMarkers = new Vector<>();

        this.context = context;
        initialiseAccidentTypeMarkers();
    }

    private void initialiseAccidentTypeMarkers() {
        accidentTypeMarkers = new HashMap<>();

        String[] accidentTypes = context.getResources().getStringArray(R.array.accidentType_array);
        BitmapDescriptor[] accidentMarkers = new BitmapDescriptor[accidentTypes.length];

        for(int i = 1; i < accidentTypes.length; i++) {
            String accidentTypeName = accidentTypes[i];
            BitmapDescriptor typeIcon
                    = BitmapDescriptorFactory.fromResource(accidentTypeMarkerIds[i-1]);
            accidentTypeMarkers.put(accidentTypeName.toLowerCase(), typeIcon);
        }
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

        int accidentId = 0;
        for(int i = 0; i < accidentMarkers.size(); i++){
            Marker marker = accidentMarkers.get(i);
            if(marker.isInfoWindowShown()) {
                accidentId = accidentList.getAccidentList().get(i).getId();
            }
            marker.remove();
        }
        accidentMarkers.clear();

        MarkerOptions markerOptions = CustomWindowInfoAdapter
                .createMarkerOptions(CustomWindowInfoAdapter.ACCIDENT_TYPE);
        List<Accident> accidents = accidentList.getAccidentList();
        for(int i = 0; i < accidents.size(); i++) {
            Accident accident = accidents.get(i);
            LatLng position = new LatLng(accident.getLat(), accident.getLon());

            markerOptions = markerOptions.snippet(accident.toString());

            if(accident.getType() != null && !accident.getType().isEmpty()) {
                BitmapDescriptor typeIcon = accidentTypeMarkers.get(accident.getType().toLowerCase());
                if(typeIcon != null) {
                    markerOptions = markerOptions.icon(typeIcon);
                }
            }

            Marker marker = gMap.addMarker(markerOptions.position(position));
            accidentMarkers.add(marker);

            if(accident.getId() == accidentId){
                marker.showInfoWindow();
            }
        }
    }
}
