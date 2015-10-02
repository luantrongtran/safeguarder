package ifn701.safeguarder.webservices;

import android.os.AsyncTask;
import android.view.View;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.MyBean;
import ifn701.safeguarder.backend.myApi.model.User;

/**
 * Created by Jeevan on 9/16/2015.
 */
public class SignupService extends AsyncTask<String, Void, User> {

    private ISignupService interfaceSignupService;

    public SignupService(ISignupService interfaceSignup) {
        this.interfaceSignupService = interfaceSignup;
    }

    @Override
    protected User doInBackground(String... params) {
       String name = params[0];
        String email = params[1];
        String password = params[2];

        MyApi myApiService = BackendApiProvider.getPatientApi();
        User user = new User();
        user.setFullName(name);
        user.setEmail(email);
        user.setPassword(password);
        try{
            user = myApiService.signup(user).execute();
            System.out.println("return string"+ user);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        System.out.println("User Exists");
        interfaceSignupService.processUserSignup(user);
    }
}
