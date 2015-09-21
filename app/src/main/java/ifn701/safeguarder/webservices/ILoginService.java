package ifn701.safeguarder.webservices;

import ifn701.safeguarder.backend.myApi.model.User;

/**
 * Created by Jeevan on 9/21/2015.
 */
public interface ILoginService {
   public void processUserLogin(User user);
}
