package ifn701.safeguarder;

import android.content.Context;
import android.util.Log;

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

import ifn701.safeguarder.CustomSharedPreferences.EventFilterSharedPreferences;
import ifn701.safeguarder.activities.CustomWindowInfoAdapter;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.backend.myApi.model.AccidentList;

public class AccidentManager {
    AccidentList accidentList;

    Vector<Marker> accidentMarkers;
    Context context;
    EventFilterSharedPreferences eventFilterSharedPreferences;

    private Map<String, BitmapDescriptor> accidentTypeMarkers;
    //accidentTypeMarkerIds should be corresponding to the accidentTypeNames
    public static int[] accidentTypeMarkerIds = {R.drawable.aviation_marker, R.drawable.crime_marker,
            R.drawable.earthquake_marker, R.drawable.ferry_marker, R.drawable.industry_accident_marker,
            R.drawable.traffic_accident_marker, R.drawable.weather_marker};
    public static int[] accidentTypeNames = {R.string.aviation_accident, R.string.criminal,
            R.string.earthquake, R.string.ferry_accident, R.string.industry_accident,
            R.string.traffic_accident, R.string.weather_accident};

    //Id used for each accident type within SharedPreferences
    public static String[] sharedPreferencesIds = {"aviation", "criminal", "earthquake", "ferry",
            "industry", "traffic", "weather"};

    public AccidentManager(Context context) {
        accidentList = new AccidentList();
        accidentMarkers = new Vector<>();

        this.context = context;
        initialiseAccidentTypeMarkers();

        eventFilterSharedPreferences = new EventFilterSharedPreferences(context);
    }

    private void initialiseAccidentTypeMarkers() {
        accidentTypeMarkers = new HashMap<>();

        for (int i = 0; i < accidentTypeNames.length; i++) {
            String accidentTypeName = context.getString(accidentTypeNames[i]);
            BitmapDescriptor typeIcon
                    = BitmapDescriptorFactory.fromResource(accidentTypeMarkerIds[i]);
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
        Log.i(Constants.APPLICATION_ID, "Update accident markers");
        if (accidentList == null || accidentList.getAccidentList() == null) {
            return;
        }

        int accidentId = 0;
        for (int i = 0; i < accidentMarkers.size(); i++) {
            Marker marker = accidentMarkers.get(i);
            if (marker.isInfoWindowShown()) {
                accidentId = accidentList.getAccidentList().get(i).getId();
            }
            marker.remove();
        }
        accidentMarkers.clear();

        MarkerOptions markerOptions = CustomWindowInfoAdapter
                .createMarkerOptions(CustomWindowInfoAdapter.ACCIDENT_TYPE);
        List<Accident> accidents = accidentList.getAccidentList();

        Map<String, Boolean> eventFilter = eventFilterSharedPreferences.getSettings();
        for (int i = 0; i < accidents.size(); i++) {
            Accident accident = accidents.get(i);

            if (accident.getType() == null || accident.getType().isEmpty()) {
                //Accident type should not be null or empty.
                continue;
            }

            //Check filter settings

            Log.i(Constants.APPLICATION_ID, accident.getType());
            if(!eventFilter.get(accident.getType())) {
                continue;
            }

            LatLng position = new LatLng(accident.getLat(), accident.getLon());

            markerOptions = markerOptions.snippet(accident.toString());

            BitmapDescriptor typeIcon = accidentTypeMarkers.get(accident.getType().toLowerCase());
            if (typeIcon != null) {
                markerOptions = markerOptions.icon(typeIcon);
            }

            Marker marker = gMap.addMarker(markerOptions.position(position));
            accidentMarkers.add(marker);

            if (accident.getId() == accidentId) {
                marker.showInfoWindow();
            }
        }
    }
}