package ifn701.safeguarder.webservices.image_uploader_service;

import android.os.AsyncTask;

import java.io.IOException;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.BlobAttributes;

/**
 * Created by lua on 13/10/2015.
 */
public class GetUploadUrlService extends AsyncTask <Void, Void, String> {

    //Local links of images stored in android device
    public String[] uploadFilesLink;

    /**
     * @param params Local links of images stored in android device
     * @return
     */
    @Override
    protected String doInBackground(Void... params) {
        //Gets upload url
        MyApi myApi = BackendApiProvider.getPatientApi();
        BlobAttributes uploadUrl = new BlobAttributes();
        try {
            uploadUrl = myApi.getUploadUrl().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadUrl.getUploadUrl().replace("lua", "192.168.0.100");
    }
//
//    @Override
//    protected void onPostExecute(BlobAttributes blobAttributes) {
//        if(blobAttributes == null && blobAttributes.getUploadUrl() == null) {
//            return;
//        }
//
//        //upload image using upload url
//        String uploadUrl = blobAttributes.getUploadUrl();
//        String[] params = new String[uploadFilesLink.length+1];
//        params[0] = uploadUrl;
//        for(int i = 0; i < uploadFilesLink.length; i++) {
//            params[i+1] = uploadFilesLink[i];
//        }
//
//        UploadImageService uploadImageService = new UploadImageService();
//        uploadImageService.execute(params);
//    }
}
