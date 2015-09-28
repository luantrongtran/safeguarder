package ifn701.safeguarder.webservices;

import android.os.AsyncTask;
import android.util.Log;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.Constants;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.AccidentList;

/**
 * Created by mutanthybrid on 21/09/2015.
 */
public class GetAccidentListByUserIdService extends AsyncTask<Integer, Void, AccidentList> {

    private IGetAccidentByUserIdService getAccidentByUserIdService = null;
    public GetAccidentListByUserIdService(IGetAccidentByUserIdService sh) {
        getAccidentByUserIdService = sh;
    }

    @Override
    protected AccidentList doInBackground(Integer... params) {
        int userid = params[0].intValue();

        Log.i(Constants.APPLICATION_ID, "PASSED USERID " + userid);

        MyApi apiGettingAccidentListByUserId = BackendApiProvider.getPatientApi();

        AccidentList accidentList = null;
        try {
            accidentList =  apiGettingAccidentListByUserId.getAccidentListByUserId(userid).execute();

            Log.i(Constants.APPLICATION_ID, accidentList.getAccidentList().size() + "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return accidentList;
    }

    @Override
    protected void onPostExecute(AccidentList accidentList) {
        if(accidentList == null){
            return;
        }

       getAccidentByUserIdService.getAccidentListByUserIdData(accidentList);
    }

}
