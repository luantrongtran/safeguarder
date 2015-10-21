package ifn701.safeguarder.webservices.image_uploader_service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.api.client.json.jackson2.JacksonFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import ifn701.safeguarder.entities.blob_images.BlobImageArray;

/**
 * Created by lua on 13/10/2015.
 */
public class ImageUploadService extends AsyncTask<Bitmap, Void, BlobImageArray> {
    String attachmentName = "bitmap";
    String attachmentFileName = "filename";
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    DataOutputStream request = null;
    HttpURLConnection httpUrlConnection = null;

    IImageUploadService interfaceImagesUploaded;

    public ImageUploadService(IImageUploadService interfaceImagesUploaded) {
        this.interfaceImagesUploaded = interfaceImagesUploaded;
    }

    @Override
    protected BlobImageArray doInBackground(Bitmap... params) {
//        Bitmap bm = params[0];

        GetUploadUrlService getUploadUrlService = new GetUploadUrlService();

        MyApi myApi = BackendApiProvider.getPatientApi();
        BlobAttributes blobAttributes = new BlobAttributes();
        try {
            blobAttributes = myApi.getUploadUrl().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String uploadUrl = blobAttributes.getUploadUrl();

//        HttpURLConnection httpUrlConnection = null;
        URL url = null;
        BlobImageArray arr = null;//each element contains a blob key and corresponding serving url
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
            request = new DataOutputStream(httpUrlConnection.getOutputStream());

            for(int i = 0; i < params.length; i++) {
                Bitmap bm = params[i];
                if(bm == null) {
                    continue;
                }
                String filename = attachmentFileName + i;
                String fieldName = attachmentName + i;
                addNewFile(filename, fieldName, bm);
            }

            //End content wrapper
            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);

            //Flush output buffer
            request.flush();

            request.close();

            InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();

            responseStream.close();
            httpUrlConnection.disconnect();

            JacksonFactory jacksonFactory = new JacksonFactory();
            arr = jacksonFactory.createJsonParser(response).parse(BlobImageArray.class);
            int size = arr.data.size();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return arr;
    }

    /**
     * Adding new bitmap file for uploading
     * @param filename
     * @param fieldName
     * @param bm
     */
    private void addNewFile(String filename, String fieldName, Bitmap bm) throws IOException{
        //Convert bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();

        request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" + fieldName +
                "\";filename=\"" + filename + "\"" + this.crlf);
        request.writeBytes(this.crlf);
        request.write(bytes);

        request.writeBytes(this.crlf);

        //Flush output buffer
        request.flush();
    }

    @Override
    protected void onPostExecute(BlobImageArray blobImageArray) {
        if(interfaceImagesUploaded == null) {
            return;
        }
        interfaceImagesUploaded.onImagesUploaded(blobImageArray);
    }
}
