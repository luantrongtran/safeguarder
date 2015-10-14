//http://stackoverflow.com/questions/11766878/sending-files-using-post-with-httpurlconnection
package ifn701.safeguarder.webservices.image_uploader_service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

import ifn701.safeguarder.backend.myApi.model.BlobAttributes;

/**
 * Created by lua on 13/10/2015.
 */
public class UploadImageService extends AsyncTask<String, Void, BlobAttributes> {
    String attachmentName = "bitmap";
    String attachmentFileName = "bitmap.bmp";
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    public static String TAG = "UploadImageService";

    public UploadImageService(){
    }

    /**
     *
     * @param params The first element is the upload url, the next strings are the url of image
     *               which are got from android gallery.
     * @return
     */
    @Override
    protected BlobAttributes doInBackground(String... params) {
        String uploadUrl = params[0];
        HttpURLConnection httpUrlConnection = null;
        URL url = null;
        try {
            url = new URL(uploadUrl);

            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);

            //Content wrapper
            DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());

            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" + this.attachmentName +
                    "\";filename=\"" + this.attachmentFileName + "\"" + this.crlf);
            request.writeBytes(this.crlf);

            //Convert bitmap to bytes
            String bitmapGallery = params[1];
            Bitmap bm = BitmapFactory.decodeFile(bitmapGallery);

            ByteBuffer byteBuffer = ByteBuffer.allocate(bm.getByteCount());
            bm.copyPixelsToBuffer(byteBuffer);
            byte[] bytes = byteBuffer.array();

            request.write(bytes);

            //End content wrapper
            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);

            //Flush output buffer
            request.flush();
            request.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        BlobAttributes blobAttributes = new BlobAttributes();
        return blobAttributes;
    }
}
