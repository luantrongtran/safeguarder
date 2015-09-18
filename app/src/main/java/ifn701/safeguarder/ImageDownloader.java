package ifn701.safeguarder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ifn701.safeguarder.Constants;

/**
 * Created by lua on 16/09/2015.
 */
public class ImageDownloader {
    public static Bitmap downloadBitmap(String imageUrl) {
        try {
            if(ImageDownloaderManager.contains(imageUrl)) {
                return ImageDownloaderManager.getAnImage(imageUrl);
            }

            InputStream inputStream = new URL(imageUrl).openStream();
            Bitmap downloadedBitmap = BitmapFactory.decodeStream(inputStream);
            ImageDownloaderManager.addANewImage(imageUrl, downloadedBitmap);
            return downloadedBitmap;
        } catch (IOException e) {
            Log.e(Constants.APPLIATION_ID, "Cannot load user's profile picture");
            e.printStackTrace();
        }
        return null;
    }
}
