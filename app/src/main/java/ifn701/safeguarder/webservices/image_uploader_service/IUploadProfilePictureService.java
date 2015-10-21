package ifn701.safeguarder.webservices.image_uploader_service;

import ifn701.safeguarder.backend.myApi.model.ResultCode;

/**
 * Created by lua on 20/10/2015.
 */
public interface IUploadProfilePictureService {
    public void onImagesUploaded(ResultCode resultCode);
}