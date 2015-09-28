package ifn701.safeguarder.entities.google_places;

import com.google.api.client.util.Key;

/**
 * Created by lua on 15/09/2015.
 */
public class Geometry {
    @Key
    public Location location;

    @Override
    public String toString() {
        return location.toString();
    }
}
