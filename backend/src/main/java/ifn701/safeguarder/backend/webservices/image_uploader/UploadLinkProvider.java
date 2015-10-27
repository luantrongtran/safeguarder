package ifn701.safeguarder.backend.webservices.image_uploader;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.utils.SystemProperty;

import ifn701.safeguarder.backend.Constants;

/**
 * Created by lua on 13/10/2015.
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.safeguarder.ifn701",
                ownerName = "backend.safeguarder.ifn701",
                packagePath = ""
        )
)
public class UploadLinkProvider  {
    @ApiMethod (name = "getUploadUrl")
    public BlobAttributes getUploadUrl() {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        String blobUploadUrl = blobstoreService.createUploadUrl("/uploadimages");

        if (SystemProperty.environment.value() !=
                SystemProperty.Environment.Value.Production) {
            //if localhost
            blobUploadUrl = blobUploadUrl.replace(Constants.computerName, Constants.IPAddress);
        }

        BlobAttributes blobAttributes = new BlobAttributes();
        blobAttributes.setUploadUrl(blobUploadUrl);
        return blobAttributes;
    }
}
