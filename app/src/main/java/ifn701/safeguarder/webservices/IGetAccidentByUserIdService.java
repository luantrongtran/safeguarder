package ifn701.safeguarder.webservices;

import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.backend.myApi.model.AccidentList;

/**
 * Created by mutanthybrid on 21/09/2015.
 */
public interface IGetAccidentByUserIdService {

    public void getAccidentListByUserIdData(AccidentList accidentList);
}
