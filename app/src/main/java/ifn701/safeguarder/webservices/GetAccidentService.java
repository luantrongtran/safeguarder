package ifn701.safeguarder.webservices;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.Constants;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.Accident;

/**
 * Created by mutanthybrid on 21/09/2015.
 */
public class GetAccidentService extends AsyncTask<Integer, Void, Accident> {
    private IGetAccidentService getAccidentService = null;

    public GetAccidentService(IGetAccidentService sh) {
        getAccidentService = sh;
    }
    @Override
    protected Accident doInBackground(Integer... params) {

        int report = params[0].intValue();

        MyApi getAccidentApiService = BackendApiProvider.getPatientApi();

        Accident accident = null;
        try {
            accident = getAccidentApiService.getAccident(report).execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return accident;
    }

    @Override
    protected void onCancelled() {
        //super.onCancelled();
        //System.out.println("Your report has not been sent.");
    }

    @Override
    protected void onPostExecute(Accident accident) {
        //super.onPostExecute(accident);
        //System.out.println("Your report has been sent.");
        if(accident == null){
            return;
        }
        getAccidentService.getAccidentData(accident);
    }
}
