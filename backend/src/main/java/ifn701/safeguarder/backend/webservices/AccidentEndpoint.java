package ifn701.safeguarder.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.inject.Named;

import ifn701.safeguarder.backend.dao.AccidentDao;
import ifn701.safeguarder.backend.entities.Accident;
import ifn701.safeguarder.backend.entities.AccidentList;
import ifn701.safeguarder.backend.entities.Location;

/**
 * An endpoint class we are exposing
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
public class AccidentEndpoint {
    private static final Logger logger = Logger.getLogger(AccidentEndpoint.class.getName());

    /**
     * @param lat    the latitude of the centroid
     * @param lon    the longtitude of the centroid
     * @param radius the radius of the centroid indicated by lat and lon
     * @return all the accidents within the radius of the centroid indicated by lat and long
     */
    @ApiMethod(name = "getAccidentInRange")
    public AccidentList getAccidents(@Named("lat") double lat, @Named("lon") double lon, @Named("radius") float radius) {
        AccidentDao accidentDao = new AccidentDao();

        Vector<Accident> accidentVector = accidentDao.getAccidentsInSelectedArea(lat, lon, radius);
        AccidentList accidentList = new AccidentList();
        accidentList.setAccidentList(accidentVector);
        return accidentList;
    }

    /**
     * This method gets the <code>Accident</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Accident</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getAccident")
    public Accident getAccident(@Named("id") int id) {
        // TODO: Implement this function
        Accident accident = accidentdao.findById(id);

        return accident;
    }

    @ApiMethod(name = "getAccidentListByUserId")
    public AccidentList getAccidentListByUserId(@Named("userId") int userId) {
        System.out.println("UserID:" + userId);
        Vector<Accident> accidents = accidentdao.findAccidentsByUserId(userId);
        AccidentList accidentList = new AccidentList();
        accidentList.setAccidentList(accidents);

        return accidentList;
    }


    /**
     * This inserts a new <code>Accident</code> object.
     *
     * @param accident The object to be added.
     * @return The object to be added.
     */


    private AccidentDao accidentdao = new AccidentDao();

    @ApiMethod(name = "insertAccident")
    public Accident insertAccident(Accident accident) {
        // TODO: Implement this function
        //logger.info("Calling insertAccident method");
        accidentdao.insertANewAccident(accident);

        return accident;
    }
}