package ifn701.safeguarder.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.List;
import java.util.Vector;

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

    /**
     *
     * @param lat the latitude of the centroid
     * @param lon the longtitude of the centroid
     * @param radius the radius of the centroid indicated by lat and lon
     * @return all the accidents within the radius of the centroid indicated by lat and long
     */
    @ApiMethod (name = "getAccidentInRange")
    public AccidentList getAccidents(@Named("lat") double lat, @Named("lon") double lon, @Named("radius")float radius) {
        AccidentDao accidentDao = new AccidentDao();

        Vector<Accident> accidentVector = accidentDao.getAccidentsInSelectedArea(lat, lon, radius);
        AccidentList accidentList = new AccidentList();
        accidentList.setAccidentList(accidentVector);
        return accidentList;
    }
}