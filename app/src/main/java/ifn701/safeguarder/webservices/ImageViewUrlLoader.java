package ifn701.safeguarder.webservices;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import ifn701.safeguarder.ImageDownloader;
import ifn701.safeguarder.Constants;


public class ImageViewUrlLoader extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    String imageUrl = "";
    IImageViewUrlLoader interfaceImageViewUrlLoader;
    public ImageViewUrlLoader(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    public ImageViewUrlLoader(ImageView imageView, IImageViewUrlLoader interfaceImageViewUrlLoader){
        this.interfaceImageViewUrlLoader = interfaceImageViewUrlLoader;
        this.bmImage = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        imageUrl = urls[0];
        Bitmap downloadedBitmap = ImageDownloader.downloadBitmap(imageUrl);

        return downloadedBitmap;
    }

    protected void onPostExecute(Bitmap result) {
        if(result == null){
            Log.e(Constants.APPLICATION_ID, "Couldn't download the image from the url: " + imageUrl);
            return;
        }
        bmImage.setImageBitmap(result);

        if(interfaceImageViewUrlLoader != null) {
            interfaceImageViewUrlLoader.onImageViewUpdated();
        }
    }
}
