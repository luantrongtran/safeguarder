/**
 * References https://github.com/ddewaele/GooglePlacesApi
 */

package ifn701.safeguarder.entities.google_places;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.util.List;

public class Place extends GenericJson{

    @Key
    public String id;

    @Key
    public String place_id;

    @Key
    public String name;

    @Key
    public String reference;

    @Key
    public Geometry geometry;

    @Key
    public List<String> types;

    @Key
    public String icon;

    @Key
    public String formatted_address;

    @Key
    public String formatted_phone_number;

    @Override
    public String toString() {
        return name + " - " + geometry + " - " + types.size();
    }

    @Key
    public List<Photo> photos;
}
