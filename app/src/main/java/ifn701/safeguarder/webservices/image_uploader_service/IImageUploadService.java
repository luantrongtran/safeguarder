package ifn701.safeguarder.webservices.image_uploader_service;

import ifn701.safeguarder.entities.blob_images.BlobImageArray;

/**
 * Created by lua on 20/10/2015.
 */
public interface IImageUploadService {
    public void onImagesUploaded(BlobImageArray blobImageArray);
}
