package ifn701.safeguarder.backend.entities;

import java.util.Vector;

/**
 * Created by lua on 3/09/2015.
 */
public class AccidentList {
    public Vector<Accident> getAccidentList() {
        return accidentList;
    }

    public void setAccidentList(Vector<Accident> accidentList) {
        this.accidentList = accidentList;
    }

    Vector<Accident> accidentList;

    public AccidentList() {
        accidentList = new Vector<>();
    }

    public int getHomeEventSize() {
        return homeEventSize;
    }

    public void setHomeEventSize(int homeEventSize) {
        this.homeEventSize = homeEventSize;
    }

    public int getCurrentLocationEventSize() {
        return currentLocationEventSize;
    }

    public void setCurrentLocationEventSize(int currentLocationEventSize) {
        this.currentLocationEventSize = currentLocationEventSize;
    }

    private int homeEventSize;
    private int currentLocationEventSize;
}
