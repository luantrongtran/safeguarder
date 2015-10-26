package ifn701.safeguarder.backend.controllers.auto_upload_events;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ifn701.safeguarder.backend.Constants;
import ifn701.safeguarder.backend.dao.AccidentDao;
import ifn701.safeguarder.backend.entities.Accident;

/**
 * Created by lua on 23/10/2015.
 */
public class RecieveUploadFilesServlet extends HttpServlet {

    private int checkFilenameBelongToWhichAccident(Accident accident, String filename) {
        if (accident.getImage1().equalsIgnoreCase(filename)) {
            return 1;
        } else if (accident.getImage2().equalsIgnoreCase(filename)) {
            return 2;
        } else if (accident.getImage3().equalsIgnoreCase(filename)) {
            return 3;
        }
        return 0;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String str = req.getParameter("numOfItems");
        int numOfItems = 0;
        if(str != null || !str.isEmpty()) {
            numOfItems = Integer.valueOf(str);
        }

        Map<String, Integer> observationLevel = new HashMap<>(4);
        observationLevel.put("highest", 4);
        observationLevel.put("high", 3);
        observationLevel.put("medium", 2);
        observationLevel.put("low", 1);

        List<Accident> lstAccidents = new ArrayList<>(numOfItems);

        String name = "name";
        String type = "type";
//        String time = "time";
        String lat = "lat";
        String lon = "lon";
        String observation = "observation";
        String description = "description";
        String image1 = "image1";
        String image2 = "image2";
        String image3 = "image3";
        String millisecond = "millisecond";
        for(int i = 0; i < numOfItems; i++) {
            String name_ = name + i;
            String type_ = type + i;
//            String time_ = time + i;
            String lat_ = lat + i;
            String lon_ = lon + i;
            String observation_ = observation + i;
            String description_ = description + i;
            String image1_ = image1 + i;
            String image2_ = image2 + i;
            String image3_ = image3 + i;
            String millisecond_ = millisecond + i;

            Accident accident = new Accident();
            if(req.getParameter(name_) != null ) {
                accident.setName(req.getParameter(name_));
            }
            if(req.getParameter(type_) != null) {
                accident.setType(req.getParameter(type_));
                if(accident.getType().trim().split(" ").length == 1) {
                    String firstEle = accident.getType().trim().split(" ")[0].trim();
                    if (!firstEle.equalsIgnoreCase("Earthquake") || !firstEle.equalsIgnoreCase("Criminal")) {
                        String temp = accident.getType() + " Accident";
                        accident.setType(temp);
                    }
                }
            }
            if(req.getParameter(millisecond_) != null) {
                accident.setTime(Long.valueOf(req.getParameter(millisecond_)));
            }
            if(req.getParameter(lat_) != null) {
                accident.setLat(Double.valueOf(req.getParameter(lat_)));
            }
            if(req.getParameter(lon_) != null) {
                accident.setLon(Double.valueOf(req.getParameter(lon_)));
            }
            if(req.getParameter(observation_) != null) {
                int obs = observationLevel.get(req.getParameter(observation_).toLowerCase());
                accident.setObservation_level(obs);
            }
            if(req.getParameter(description_) != null) {
                accident.setDescription(req.getParameter(description_));
            }
            if(req.getParameter(image1_) != null) {
                accident.setImage1(req.getParameter(image1_));
            }
            if(req.getParameter(image2_) != null) {
                accident.setImage2(req.getParameter(image2_));
            }
            if(req.getParameter(image3_) != null) {
                accident.setImage3(req.getParameter(image3_));
            }


            lstAccidents.add(accident);
        }

        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> map = blobstoreService.getUploads(req);

        if(map.size() > 0) {
            List<BlobKey> blobKeys = map.get("images");
            BlobKey blobKey = blobKeys.get(0);

            InputStream inputStream = new BlobstoreInputStream(blobKey);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String fileName = entry.getName().toLowerCase();

                byte[] data = IOUtils.toByteArray(zipInputStream);

                GcsFilename gcsFilename = new GcsFilename(Constants.GoogleCloudBucket, fileName);
                GcsService gcsService =
                        GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
                @SuppressWarnings("resource")
                GcsOutputChannel outputChannel =
                        gcsService.createOrReplace(gcsFilename, GcsFileOptions.getDefaultInstance());
                int b = outputChannel.write(ByteBuffer.wrap(data));
                outputChannel.close();

                String gs_blob_key = "/gs/" + Constants.GoogleCloudBucket + "/" + fileName;
                BlobKey blob_key = BlobstoreServiceFactory.getBlobstoreService().createGsBlobKey(gs_blob_key);
                ServingUrlOptions serving_options = ServingUrlOptions.Builder.withBlobKey(blob_key);
                String serving_url = ImagesServiceFactory.getImagesService().getServingUrl(serving_options);
                System.out.println("Serving URL: " + serving_url);
                resp.getWriter().println(serving_url);

                for(Accident acc : lstAccidents) {
                    int index = checkFilenameBelongToWhichAccident(acc, fileName);
                    if(index == 1) {
                        acc.setImage1(serving_url);
                    } else if (index == 2) {
                        acc.setImage2(serving_url);
                    } else if (index == 3) {
                        acc.setImage3(serving_url);
                    }
                }
            }
        }

        AccidentDao accidentDao = new AccidentDao();
        //modify invalid image to empty string, and re-arrange image into the order of image1,
        //image 2 and image3
        for(Accident acc : lstAccidents) {
            if(!acc.getImage1().startsWith("http")) {
                //if image1 is not valid
                if (acc.getImage2().startsWith("http")) {
                    //if image2 is valid, then replace image1 with image2, and make image2 invalid
                    acc.setImage1(acc.getImage2());
                    acc.setImage2("");
                } else if (acc.getImage3().startsWith("http")) {
                    //if image3 is valid, then replace image1 with image3, and make image3 invalid
                    acc.setImage1(acc.getImage3());
                    acc.setImage3("");
                } else {
                    acc.setImage1("");
                }
            }
            if (!acc.getImage2().startsWith("http")) {
                //if image2 is invalid
                if(acc.getImage3().startsWith("http")) {
                    //if image3 is valid, replace image 2 with image3 and make image3 invalid
                    acc.setImage2(acc.getImage3());
                    acc.setImage3("");
                } else {
                    acc.setImage2("");
                }
            }
            if(!acc.getImage3().startsWith("http")) {
                acc.setImage3("");
            }

            acc.setUserId(Constants.administrator_id);
            accidentDao.insertANewAccident(acc);
        }
    }
}
