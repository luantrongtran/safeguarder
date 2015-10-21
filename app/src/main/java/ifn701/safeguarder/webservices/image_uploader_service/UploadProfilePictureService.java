//http://stackoverflow.com/questions/11766878/sending-files-using-post-with-httpurlconnection
package ifn701.safeguarder.webservices.image_uploader_service;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.backend.myApi.model.ResultCode;
import ifn701.safeguarder.entities.blob_images.BlobImage;
import ifn701.safeguarder.entities.blob_images.BlobImageArray;

/**
 * Created by lua on 13/10/2015.
 */
public class UploadProfilePictureService extends AsyncTask<Bitmap, Void, Bitmap[]>
            implements IImageUploadService, IUpdateProfilePictureUrlService{

    private long uploadingTimeOut = 3000;

    public static String TAG = "UploadImageService";
    IUploadProfilePictureService interfaceUploadProfilePic;
    BlobImage blobImage;

    Context context;
    public UploadProfilePictureService(Context context, IUploadProfilePictureService interfaceUploadProfilePic){
        this.context = context;
        this.interfaceUploadProfilePic = interfaceUploadProfilePic;
    }

    @Override
    protected Bitmap[] doInBackground(Bitmap... params) {
        return params;
    }

    @Override
    protected void onPostExecute(Bitmap[] bitmaps) {

        ImageUploadService imageUploadService = new ImageUploadService(this);

        ResultCode result = new ResultCode();
        result.setResult(false);
        imageUploadService.execute(bitmaps);
    }

    @Override
    public void onImagesUploaded(BlobImageArray blobImageArray) {
        ResultCode result = new ResultCode();
        result.setResult(false);

        if (blobImageArray != null && blobImageArray.data.size() > 0) {
            blobImage = blobImageArray.data.get(0);
            UpdateProfilePictureUrlService updateProfilePictureUrlService
                    = new UpdateProfilePictureUrlService(context, this);

            updateProfilePictureUrlService.execute(blobImage);
            //There is only 1 picture returned when upload profile picture.
        }
    }

    @Override
    public void onProfilePictureUrlUpdated(ResultCode result) {
        if(blobImage != null && blobImage.servingUrl != null && result != null
                && result.getResult() != null && result.getResult()) {
            UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);
            userInfoPreferences.setProfilePicture(blobImage.servingUrl);
        }
        interfaceUploadProfilePic.onImagesUploaded(result);
    }
}
