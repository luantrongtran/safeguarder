package ifn701.safeguarder.backend.webservices.image_uploader;

/**
 * Created by lua on 13/10/2015.
 */
public class BlobAttributes {
    private String uploadUrl;

    public String getServingUrl() {
        return servingUrl;
    }

    public void setServingUrl(String servingUrl) {
        this.servingUrl = servingUrl;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    private String servingUrl;
}
