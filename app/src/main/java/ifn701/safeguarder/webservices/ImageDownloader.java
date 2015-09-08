package ifn701.safeguarder.webservices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ifn701.safeguarder.Constants;


public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public ImageDownloader(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String imageUrl = urls[0];
        Bitmap downloadedBitmap = null;

        try {
            InputStream inputStream = new URL(imageUrl).openStream();
            downloadedBitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e(Constants.APPLIATION_ID, "Cannot load user's profile picture");
            e.printStackTrace();
        }

        return downloadedBitmap;
    }

    protected void onPostExecute(Bitmap result) {
        if(result == null){
            return;
        }
        bmImage.setImageBitmap(result);
    }
}
