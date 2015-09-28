package ifn701.safeguarder.entities.google_places;

import java.util.List;

import com.google.api.client.util.Key;

public class PlacesList {

    @Key
    public String status;

    @Key
    public List<Place> results;

    @Key
    public String next_page_token;
}
