package ifn701.safeguarder.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.logging.Logger;

import javax.inject.Named;

import ifn701.safeguarder.backend.entities.Accident;
import ifn701.safeguarder.backend.dao.AccidentDao;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "accidentApi",
        version = "v1",
        resource = "accident",
        namespace = @ApiNamespace(
                ownerDomain = "entities.backend.safeguarder.ifn701",
                ownerName = "entities.backend.safeguarder.ifn701",
                packagePath = ""
        )
)
public class AccidentEndpoint {

    private static final Logger logger = Logger.getLogger(AccidentEndpoint.class.getName());

    /**
     * This method gets the <code>Accident</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Accident</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getAccident")
    public Accident getAccident(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getAccident method");
        return null;
    }

    /**
     * This inserts a new <code>Accident</code> object.
     *
     * @param accident The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertAccident")
    public Accident insertAccident(Accident accident) {
        // TODO: Implement this function
        //logger.info("Calling insertAccident method");
        AccidentDao accidentdao = new AccidentDao();
        accidentdao.insertANewAccident(accident);

        return accident;
    }
}