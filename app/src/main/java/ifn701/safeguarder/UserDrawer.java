package ifn701.safeguarder;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;

/**
 * Created by lua on 7/09/2015.
 */
public class UserDrawer {
    private Circle currentLocationInterestedArea;
    private int currentLocationInterestedAreaFillColor = 0x330000FF;
    private int currentLocationInterestedAreaStrokeColor = 0x800000CC;

    private Context context;
    private GoogleMap gMap;

    public UserDrawer (Context context, GoogleMap gMap) {
        this.context = context;
        this.gMap = gMap;
        drawCurrentLocationInterestedArea();
    }

    public void drawCurrentLocationInterestedArea() {
        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(context);
        UserSettingsPreferences userSettingsPreferences = new UserSettingsPreferences(context);

        LatLng position = new LatLng(currentLocationPreferences.getLat(),
                currentLocationPreferences.getLon());
        float radius = userSettingsPreferences.getRadius();
        CircleOptions circleOptions = new CircleOptions()
                .center(position)
                .radius(radius)
                .fillColor(currentLocationInterestedAreaFillColor)
                .strokeColor(currentLocationInterestedAreaStrokeColor);

        currentLocationInterestedArea = gMap.addCircle(circleOptions);
    }

    public void updateCurrentLocationInterestedArea(){
        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(context);
        UserSettingsPreferences userSettingsPreferences = new UserSettingsPreferences(context);

        LatLng position = new LatLng(currentLocationPreferences.getLat(),
                currentLocationPreferences.getLon());
        float radius = userSettingsPreferences.getRadius();

        currentLocationInterestedArea.setCenter(position);
        currentLocationInterestedArea.setRadius(radius);
    }
}
