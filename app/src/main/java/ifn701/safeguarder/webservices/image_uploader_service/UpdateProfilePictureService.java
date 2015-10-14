package ifn701.safeguarder.webservices.image_uploader_service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.BlobAttributes;

/**
 * Created by lua on 13/10/2015.
 */
public class UpdateProfilePictureService extends AsyncTask<Bitmap, Void, String> {
    String attachmentName = "bitmap";
    String attachmentFileName = "bitmap.bmp";
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    String filename ;
    Context context;
    public UpdateProfilePictureService(Context context, String filename) {
        this.context = context;
        this.filename = filename;
    }

    @Override
    protected String doInBackground(Bitmap... params) {
        Bitmap bm = params[0];

        GetUploadUrlService getUploadUrlService = new GetUploadUrlService();

        MyApi myApi = BackendApiProvider.getPatientApi();
        BlobAttributes blobAttributes = new BlobAttributes();
        try {
            blobAttributes = myApi.getUploadUrl().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String uploadUrl = blobAttributes.getUploadUrl().replace("lua","172.19.16.119");

        HttpURLConnection httpUrlConnection = null;
        URL url = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(uploadUrl);

            FileOutputStream out = null;

            MultipartEntity reqEntity = new MultipartEntity();
            File f = new File(filename);
            FileBody fileBody = new FileBody(f);
            reqEntity.addPart("file", fileBody);

            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
//            url = new URL(uploadUrl);
//
//            httpUrlConnection = (HttpURLConnection) url.openConnection();
//            httpUrlConnection.setUseCaches(false);
//            httpUrlConnection.setDoOutput(true);
//
//            httpUrlConnection.setRequestMethod("POST");
//            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
//            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
//            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);
//
//            //Content wrapper
//            DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());
//
//            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
//            request.writeBytes("Content-Disposition: form-data; name=\"" + this.attachmentName +
//                    "\";filename=\"" + this.attachmentFileName + "\"" + this.crlf);
//            request.writeBytes(this.crlf);
//
//            ByteBuffer byteBuffer = ByteBuffer.allocate(bm.getByteCount());
//            bm.copyPixelsToBuffer(byteBuffer);
//            byte[] bytes = byteBuffer.array();
//
//            request.write(bytes);
//
//            //End content wrapper
//            request.writeBytes(this.crlf);
//            request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);
//
//            //Flush output buffer
//            request.flush();
//            request.close();
//
//            InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());
//
//            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
//            String line = "";
//            StringBuilder stringBuilder = new StringBuilder();
//            while ((line = responseStreamReader.readLine()) != null)
//            {
//                stringBuilder.append(line).append("\n");
//            }
//            responseStreamReader.close();
//
//            String response = stringBuilder.toString();
//
//            responseStream.close();
//            httpUrlConnection.disconnect();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
