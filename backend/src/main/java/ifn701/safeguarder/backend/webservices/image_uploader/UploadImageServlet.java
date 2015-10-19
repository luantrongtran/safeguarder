package ifn701.safeguarder.backend.webservices.image_uploader;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.utils.SystemProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ifn701.safeguarder.backend.Constants;
import ifn701.safeguarder.backend.dao.UserDao;

/**
 * Created by lua on 13/10/2015.
 */
public class UploadImageServlet extends HttpServlet {
    String fieldName = "bitmap";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

        Map<String, List<BlobKey>> map = blobstoreService.getUploads(req);

        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        JSONArray jsonArr = new JSONArray();
        for (int i = 0; i < map.size(); i++) {
            List<BlobKey> blobs = map.get(fieldName + i);
            BlobKey blobKey = blobs.get(0);

            ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);
            String servingUrl = imagesService.getServingUrl(servingOptions);
            if (SystemProperty.environment.value() !=
                    SystemProperty.Environment.Value.Production) {
                //if localhost
                servingUrl = servingUrl.replace("0.0.0.0", Constants.IPAddress);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("blobKey", blobKey.getKeyString());
            jsonObject.put("servingUrl", servingUrl);

            jsonArr.put(jsonObject);
        }

//        List<BlobKey> blobs = map.get("bitmap");
//        BlobKey blobKey = blobs.get(0);

//        ImagesService imagesService = ImagesServiceFactory.getImagesService();
//        ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);
//
//        String servingUrl = imagesService.getServingUrl(servingOptions);
//        System.out.println("Serving url: " + servingUrl);

//        if(SystemProperty.environment.value() !=
//                SystemProperty.Environment.Value.Production) {
//            //if localhost
//            servingUrl = servingUrl.replace("0.0.0.0", Constants.IPAddress);
//        }

        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json");

        JSONObject json = new JSONObject();
        json.put("data", jsonArr);

        PrintWriter out = res.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();
    }
}
