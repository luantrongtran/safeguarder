package ifn701.safeguarder.webservices;

import ifn701.safeguarder.backend.myApi.model.User;

/**
 * Created by Jeevan on 10/2/2015.
 */
public interface ISignupService {
    public void processUserSignup(User user);
}
