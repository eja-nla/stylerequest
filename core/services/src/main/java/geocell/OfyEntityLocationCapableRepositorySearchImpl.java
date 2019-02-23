package geocell;

import static com.hair.business.dao.datastore.ofy.OfyService.ofy;

import java.util.List;

import geocell.model.GeoLocation;

/**
 * Created by olukoredeaguda on 12/02/2019.
 */
public class OfyEntityLocationCapableRepositorySearchImpl implements LocationCapableRepositorySearch<GeoLocation> {

    @Override
    public List<GeoLocation> search(List<String> geocells) {
        return ofy().load().type(GeoLocation.class).filter("geocells in ", geocells).list();
    }

}

