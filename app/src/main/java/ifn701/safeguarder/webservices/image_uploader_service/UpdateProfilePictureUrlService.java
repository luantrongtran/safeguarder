package ifn701.safeguarder.webservices.image_uploader_service;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.ResultCode;
import ifn701.safeguarder.entities.blob_images.BlobImage;

/**
 * Created by lua on 20/10/2015.
 */
public class UpdateProfilePictureUrlService extends AsyncTask<BlobImage, Void, ResultCode> {

    Context context;
    IUpdateProfilePictureUrlService interfaceUpdateProfileUrl;

    public UpdateProfilePictureUrlService
            (Context context, IUpdateProfilePictureUrlService interfaceUpdateProfileUrl) {
        this.context = context;
        this.interfaceUpdateProfileUrl = interfaceUpdateProfileUrl;
    }

    @Override
    protected ResultCode doInBackground(BlobImage... params) {
        ResultCode resultCode = new ResultCode();
        resultCode.setResult(false);
        if(params == null || params.length == 0) {
            return resultCode;
        }

        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);
        int userId = userInfoPreferences.getUserId();
        String imageUrl = params[0].servingUrl;

        MyApi myApi = BackendApiProvider.getPatientApi();

        try {
            imageUrl = URLEncoder.encode(imageUrl, "UTF-8");
            resultCode = myApi.updateProfilePictureUrl(userId, imageUrl).execute();
//            myApi.getUser(1l).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultCode;
    }

    @Override
    protected void onPostExecute(ResultCode resultCode) {
        if(interfaceUpdateProfileUrl == null){
            return;
        }

        interfaceUpdateProfileUrl.onProfilePictureUrlUpdated(resultCode);
    }
}
