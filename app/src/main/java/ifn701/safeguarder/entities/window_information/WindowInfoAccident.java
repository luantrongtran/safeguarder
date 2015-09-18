package ifn701.safeguarder.entities.window_information;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import ifn701.safeguarder.backend.myApi.model.Accident;

/**
 * This class will be converted to JSon String which will be passed to the snippet of a google
 * marker
 */
public class WindowInfoAccident extends GenericJson {
    @Key
    private String type = WindowInfoAccident.class.getCanonicalName();

    public Accident getAccidentInformation() {
        return accidentInformation;
    }

    public void setAccidentInformation(Accident accidentInformation) {
        this.accidentInformation = accidentInformation;
    }

    public String getType() {
        return type;
    }

    @Key
    private Accident accidentInformation;
}
