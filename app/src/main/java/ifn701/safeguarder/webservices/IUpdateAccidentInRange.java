package ifn701.safeguarder.webservices;

import ifn701.safeguarder.backend.myApi.model.AccidentList;

/**
 * Created by lua on 13/09/2015.
 */
public interface IUpdateAccidentInRange {
    public void onAccidentsInRangeUpdated(AccidentList accidentList);
}
