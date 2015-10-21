package ifn701.safeguarder.webservices;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.Accident;

public class AccidentService extends AsyncTask<Accident, Void, Accident>{

    private IAccidentService accidentService = null;

    public AccidentService(IAccidentService sh) {
        accidentService = sh;
    }
    @Override
    protected Accident doInBackground(Accident... params) {
        MyApi.Builder accidentApi = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null);

        accidentApi.setRootUrl(Constants.webServiceUrl);

        //when running on google app engine disable zip function
        accidentApi.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
            @Override
            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                abstractGoogleClientRequest.setDisableGZipContent(true);
            }
        });
        Accident report = params[0];

        MyApi accidentApiService = accidentApi.build();
        Accident accident = null;
        try {
            accident = accidentApiService.insertAccident(report).execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return accident;
    }

    @Override
    protected void onCancelled() {
        //super.onCancelled();
        System.out.println("Your report has not been sent.");
    }

    @Override
    protected void onPostExecute(Accident accident) {
        //super.onPostExecute(accident);
        System.out.println("Your report has been sent.");
        accidentService.onNewAccidentReported(accident);
    }
}