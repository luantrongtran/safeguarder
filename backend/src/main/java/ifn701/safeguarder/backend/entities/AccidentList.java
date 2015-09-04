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
}
