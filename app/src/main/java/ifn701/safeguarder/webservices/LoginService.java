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
public class LoginService extends AsyncTask<String, Void, User> {

    private ILoginService interfaceLoginService;

    public LoginService(ILoginService interfaceLogin) {
        this.interfaceLoginService = interfaceLogin;
    }

    @Override
    protected User doInBackground(String... params) {
        String email = params[0];
        String password = params[1];

        MyApi myApiService = BackendApiProvider.getPatientApi();
        User user = null;
        try{
            user = myApiService.login(email,password).execute();
            System.out.println("return string"+ user);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        System.out.println("User Exists");
        interfaceLoginService.processUserLogin(user);
    }
}
