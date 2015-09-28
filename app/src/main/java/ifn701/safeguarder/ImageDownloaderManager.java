package ifn701.safeguarder;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lua on 16/09/2015.
 */
public class ImageDownloaderManager {
    private static Map<String, Bitmap> downloadedImages = new HashMap<>();

    public static void addANewImage(String url, Bitmap value) {
        downloadedImages.put(url, value);
    }

    public static boolean contains(String url) {
        return downloadedImages.containsKey(url);
    }

    public static void deleteAImage(String url) {
        downloadedImages.remove(url);
    }

    public static Bitmap getAnImage(String url) {
        return downloadedImages.get(url);
    }
}
