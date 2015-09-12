/**
 * References https://github.com/ddewaele/GooglePlacesApi
 */

package ifn701.safeguarder.entities;
import com.google.api.client.util.Key;

public class Place {

    @Key
    public String id;

    @Key
    public String name;

    @Key
    public String reference;

    @Override
    public String toString() {
        return name + " - " + id + " - " + reference;
    }

}
