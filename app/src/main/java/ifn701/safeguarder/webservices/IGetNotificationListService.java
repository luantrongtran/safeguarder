package ifn701.safeguarder.webservices;

import java.util.List;

import ifn701.safeguarder.backend.myApi.model.Accident;

/**
 * Created by lua on 4/10/2015.
 */
public interface IGetNotificationListService {
    public void onNotificationListUpdated(List<Accident> accidentList);
}
