package ifn701.safeguarder;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;

/**
 * Created by lua on 7/09/2015.
 */
public class UserDrawer {
    private Circle currentLocationInterestedArea;
    private CircleOptions circleOptionsCurrentLocation;

    private Circle homeLocationInterestedArea;
    private CircleOptions circleOptionsHomeLocation;

    private Marker homeLocationMarker;


    private int currentLocationInterestedAreaFillColor = 0x330000FF;
    private int currentLocationInterestedAreaStrokeColor = 0x800000CC;

    private Context context;
    private GoogleMap gMap;

    CurrentLocationPreferences currentLocationPreferences;
    UserSettingsPreferences userSettingsPreferences;

    public UserDrawer (Context context, GoogleMap gMap) {
        this.context = context;
        this.gMap = gMap;

        circleOptionsCurrentLocation = new CircleOptions()
                .fillColor(currentLocationInterestedAreaFillColor)
                .strokeColor(currentLocationInterestedAreaStrokeColor);
        circleOptionsHomeLocation = new CircleOptions()
                .fillColor(currentLocationInterestedAreaFillColor)
                .strokeColor(currentLocationInterestedAreaStrokeColor);

        if(currentLocationInterestedArea == null) {
            drawCurrentLocationInterestedArea();
        }
        if(homeLocationInterestedArea == null) {
            drawHomeLocation();
        }

        currentLocationPreferences = new CurrentLocationPreferences(context);
        userSettingsPreferences = new UserSettingsPreferences(context);
    }

    public void drawCurrentLocationInterestedArea() {
        currentLocationPreferences
                = new CurrentLocationPreferences(context);
        UserSettingsPreferences userSettingsPreferences = new UserSettingsPreferences(context);

        LatLng position = new LatLng(currentLocationPreferences.getLat(),
                currentLocationPreferences.getLon());
        float radius = userSettingsPreferences.getRadius();
        circleOptionsCurrentLocation = circleOptionsCurrentLocation
                .center(position)
                .radius(radius);

        currentLocationInterestedArea = gMap.addCircle(circleOptionsCurrentLocation);
    }

    public void updateCurrentLocationInterestedArea(){
        if(currentLocationInterestedArea == null) {
            drawCurrentLocationInterestedArea();
            return;
        }
        currentLocationPreferences
                = new CurrentLocationPreferences(context);
        userSettingsPreferences = new UserSettingsPreferences(context);

        LatLng position = new LatLng(currentLocationPreferences.getLat(),
                currentLocationPreferences.getLon());
        float radius = userSettingsPreferences.getRadius();

        currentLocationInterestedArea.setCenter(position);
        currentLocationInterestedArea.setRadius(radius);
    }

    public void drawHomeLocation() {
        userSettingsPreferences = new UserSettingsPreferences(context);
        if(userSettingsPreferences.getHomeLocationAddress().isEmpty()) {
            Log.i(Constants.APPLICATION_ID, "Home location is not set up in SharedPreferences");
            return;
        }

        LatLng position = new LatLng(userSettingsPreferences.getHomeLocationLat(),
                userSettingsPreferences.getHomeLocationLon());
        float radius = userSettingsPreferences.getRadius();
        circleOptionsHomeLocation = circleOptionsHomeLocation
                .center(position)
                .radius(radius);

        MarkerOptions markerOptions = new MarkerOptions().position(position);

        homeLocationInterestedArea = gMap.addCircle(circleOptionsHomeLocation);

        String snippet = context.getString(R.string.window_info_home_location_address) +
                userSettingsPreferences.getHomeLocationAddress();
        markerOptions.snippet(snippet)
                .title(context.getString(R.string.window_info_home_location_title));
        homeLocationMarker = gMap.addMarker(markerOptions);
    }

    public void updateHomeLocation() {
        float radius = userSettingsPreferences.getRadius();
        double lat = userSettingsPreferences.getHomeLocationLat();
        double lon = userSettingsPreferences.getHomeLocationLon();

        homeLocationInterestedArea.setRadius(radius);
        homeLocationInterestedArea.setCenter(new LatLng(lat, lon));

//        homeLocationInterestedArea = gMap.addCircle(cir)
    }
}
