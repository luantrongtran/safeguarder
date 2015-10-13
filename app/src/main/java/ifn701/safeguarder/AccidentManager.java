package ifn701.safeguarder;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
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
import ifn701.safeguarder.CustomSharedPreferences.NewAccidentWithinCurrentLocationSharedPreferences;
import ifn701.safeguarder.CustomSharedPreferences.NewAccidentWithinHomeLocationSharedPreferences;
import ifn701.safeguarder.activities.CustomWindowInfoAdapter;
import ifn701.safeguarder.activities.EventFilterActivity;
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

    //newAccidentTypeMarkers should be coreresponding to the newAccidentTypeMarkerIds
    private Map<String, BitmapDescriptor> newAccidentTypeMarkers;
    public static int[] newAccidentTypeMarkerIds = {R.drawable.aviation_marker_1, R.drawable.crime_marker_1,
            R.drawable.earthquake_marker_1, R.drawable.ferry_marker_1, R.drawable.industry_accident_marker_1,
            R.drawable.traffic_accident_marker_1, R.drawable.weather_marker_1};

    public static int[] accidentTypeNames = {R.string.aviation_accident, R.string.criminal,
            R.string.earthquake, R.string.ferry_accident, R.string.industry_accident,
            R.string.traffic_accident, R.string.weather_accident};

    NewAccidentWithinCurrentLocationSharedPreferences newAccidentCurrentLocation;
    NewAccidentWithinHomeLocationSharedPreferences newAccidentHomeLocation;

    //Id used for each accident type within SharedPreferences
    public static String[] sharedPreferencesIds = {"aviation", "criminal", "earthquake", "ferry",
            "industry", "traffic", "weather"};

    public AccidentManager(Context context) {
        accidentList = new AccidentList();
        accidentMarkers = new Vector<>();

        this.context = context;
        initialiseAccidentTypeMarkers();

        eventFilterSharedPreferences = new EventFilterSharedPreferences(context);
        newAccidentCurrentLocation = new NewAccidentWithinCurrentLocationSharedPreferences(context);
        newAccidentHomeLocation = new NewAccidentWithinHomeLocationSharedPreferences(context);
    }

    private void initialiseAccidentTypeMarkers() {
        accidentTypeMarkers = new HashMap<>();
        for (int i = 0; i < accidentTypeNames.length; i++) {
            String accidentTypeName = context.getString(accidentTypeNames[i]);
            BitmapDescriptor typeIcon
                    = BitmapDescriptorFactory.fromResource(accidentTypeMarkerIds[i]);
            accidentTypeMarkers.put(accidentTypeName.toLowerCase(), typeIcon);
        }

        newAccidentTypeMarkers = new HashMap<>();
        for (int i = 0; i < accidentTypeNames.length; i++) {
            String accidentTypeName = context.getString(accidentTypeNames[i]);
            BitmapDescriptor typeIcon
                    = BitmapDescriptorFactory.fromResource(newAccidentTypeMarkerIds[i]);
            newAccidentTypeMarkers.put(accidentTypeName.toLowerCase(), typeIcon);
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
        long current = System.currentTimeMillis();
        long timeFilter = eventFilterSharedPreferences.getTimeSetting();
        for (int i = 0; i < accidents.size(); i++) {
            Accident accident = accidents.get(i);

            if (accident.getType() == null || accident.getType().isEmpty()) {
                //Accident type should not be null or empty.
                continue;
            }

            //Check filter settings
            Log.i(Constants.APPLICATION_ID, accident.getType());
            if(!eventFilter.get(accident.getType())) {
                //filter accidents by type
                continue;
            }

            if(timeFilter != EventFilterActivity.DISPLAY_ALL) {
                //filter by time
                long interval = current - accident.getTime();
                Log.i(Constants.APPLICATION_ID, current+"-" + accident.getTime() +">"+timeFilter);
                if(interval > timeFilter) {
                    continue;
                }
            }

            LatLng position = new LatLng(accident.getLat(), accident.getLon());

            markerOptions = markerOptions.snippet(accident.toString());
            Log.i(Constants.APPLICATION_ID, "Marker snippet: " + accident.toString());

            BitmapDescriptor typeIcon = null;
            if(newAccidentCurrentLocation.contains(accident.getId()) ||
                    newAccidentHomeLocation.contains(accident.getId())) {
                typeIcon = newAccidentTypeMarkers.get(accident.getType().toLowerCase());
            } else {
                typeIcon = accidentTypeMarkers.get(accident.getType().toLowerCase());
            }
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

    /**
     * Update the marker of an accident after being clicked. In particular, if the marker is a
     * marker of a new accident, then redraw it and remove it from SharedPreferences
     */
    public void updateANewAccident(int accidentId) {
        int index = -1;
        Accident selectedAccident = null;
        for(int i = 0; i < accidentList.getAccidentList().size(); i++) {
            Accident accident = accidentList.getAccidentList().get(i);
            if(accident.getId() == accidentId) {
                index = i;
                selectedAccident = accident;
                break;
            }
        }

        if(index == -1) {
            return;
        }

        accidentMarkers.get(index).setIcon(newAccidentTypeMarkers.get(selectedAccident.getType()));
        removeAnAccidentFromNotification(selectedAccident);
    }

    /**
     * Update the marker of an accident after being clicked. In particular, if the marker is a
     * marker of a new accident, then redraw it and remove it from SharedPreferences
     */
    public void updateANewMarker(Marker marker) {
        int index = accidentMarkers.indexOf(marker);
        if(index == -1){
            //Couldn't find the given marker
            return;
        }

        Accident selectedAccident = accidentList.getAccidentList().get(index);
        BitmapDescriptor bitmapDescriptor = accidentTypeMarkers.get(selectedAccident.getType().toLowerCase());
        marker.setIcon(bitmapDescriptor);

        removeAnAccidentFromNotification(selectedAccident);
    }

    public void removeAnAccidentFromNotification(Accident accident) {
        newAccidentCurrentLocation.remove(accident.getId() + "");
        newAccidentHomeLocation.remove(accident.getId() + "");
    }


}