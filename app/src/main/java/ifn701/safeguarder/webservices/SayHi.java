package ifn701.safeguarder.webservices;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.MyBean;

public class SayHi extends AsyncTask<String, Void, MyBean> {

    private ISayHi sayHi = null;

    public SayHi(ISayHi sh) {
        sayHi = sh;
    }

    @Override
    protected MyBean doInBackground(String... params) {
        MyApi.Builder myApi = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null);

        myApi.setRootUrl(Constants.webServiceUrl);

        //when running on google app engine disable zip function
        myApi.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
            @Override
            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                abstractGoogleClientRequest.setDisableGZipContent(true);
            }
        });
        String name = params[0];

        MyApi myApiService = myApi.build();
        MyBean myBean = null;
        try {
            myBean = myApiService.sayHi(name).execute();
            System.out.println("return string: " + myBean) ;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return myBean;
    }

    @Override
    protected void onPostExecute(MyBean myBean) {
//        super.onPostExecute(myBean);
        System.out.println("Bean: " + myBean);
        sayHi.processData(myBean.getData());
    }
}
