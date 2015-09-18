package ifn701.safeguarder.entities.google_places;

import com.google.api.client.util.Key;

public class Location {
    @Key
    public double lat;

    @Key
    public double lng;

    @Override
    public String toString() {
        return lat + ", " + lng;
    }
}
