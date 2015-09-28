package ifn701.safeguarder.webservices;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import ifn701.safeguarder.ImageDownloader;
import ifn701.safeguarder.Constants;

/**
 * Created by lua on 16/09/2015.
 */
public class MarkerIconUrlLoader extends AsyncTask<String, Void, Bitmap> {

    String imageUrl = "";

    public MarkerIconUrlLoader() {
    }

    @Override
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
//        marker.setIcon(BitmapDescriptorFactory.fromBitmap(result));
    }
}
