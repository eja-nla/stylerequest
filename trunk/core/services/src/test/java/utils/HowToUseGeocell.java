/*
Copyright 2010 Alexandre Gellibert

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at http://www.apache.org/licenses/
LICENSE-2.0 Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS"
BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
implied. See the License for the specific language governing permissions
and limitations under the License.
*/

package utils;

import static com.hair.business.dao.datastore.ofy.OfyService.ofy;

import com.hair.business.services.customer.AbstractServicesTestBase;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import geocell.GeocellManager;
import geocell.LocationCapableRepositorySearch;
import geocell.OfyEntityLocationCapableRepositorySearchImpl;
import geocell.model.BoundingBox;
import geocell.model.CostFunction;
import geocell.model.GeoLocation;
import geocell.model.Point;

/**
 * Unit test also used to explain how to use Geocell class.
 *
 * @author Alexandre Gellibert <alexandre.gellibert@gmail.com>
 *
 */
public class HowToUseGeocell extends AbstractServicesTestBase {

    private final Logger log = Logger.getLogger("com.beoui.utils");

    LocationCapableRepositorySearch<GeoLocation> ofySearch = new OfyEntityLocationCapableRepositorySearchImpl();

    /**
     * First step is to save your entities.
     * In database, you don't save only latitude and longitude of your point but also geocells around this point.
     */
    public void testHowToSaveGeocellsInDatabase() {
        // Incoming data: latitude and longitude (Bordeaux for instance)
        double lat = 44.838611;
        double lon = -0.578333;

        // Transform it to a point
        Point p = new Point(lat, lon);

        // Generates the list of GeoCells
        List<String> cells = GeocellManager.generateGeoCell(p);

        // Save your instance
        GeoLocation obj = new GeoLocation();
        obj.setLatitude(lat);
        obj.setLongitude(lon);
        obj.setGeocells(cells);

        //objDao.save(obj);

        // Just checking that cells are not empty
        Assert.assertTrue(cells.size() > 0);

        // Show in the log what cells are going to be saved
        log.log(Level.INFO, "Geocells to be saved for Point("+lat+","+lon+") are: "+cells);
    }

    /**
     * Second step, now entities are in database, we can query on them.
     * Here is the example of a bounding box query.
     *
     */
    public void testHowToQueryOnABoundingBox() {
        // Incoming data: latitude and longitude of south-west and north-east points (around Bordeaux for instance =) )
        double latSW = 44.8;
        double lonSW = -0.6;

        double latNE = 44.9;
        double lonNE = -0.7;

        // Transform this to a bounding box
        BoundingBox bb = new BoundingBox(latNE, lonNE, latSW, lonSW);

        // Calculate the geocells list to be used in the queries (optimize list of cells that complete the given bounding box)
        List<String> cells = GeocellManager.bestBboxSearchCells(bb, null);

        // OR if you want to use a custom "cost function"
        List<String> cells2 = GeocellManager.bestBboxSearchCells(bb, new CostFunction() {

            @Override
            public double defaultCostFunction(int numCells, int resolution) {
                if(numCells > 100) {
                    return Double.MAX_VALUE;
                } else {
                    return 0;
                }
            }
        });

        // Use this in a query
        // In Google App Engine, you'll have something like below. In hibernate (or whatever else), it might be a little bit different.
//		String queryString = "select from GeoLocation where geocellsParameter.contains(geocells)";
//		Query query = pm.newQuery(query);
//	    query.declareParameters("String geocellsParameter");
//	    query.declareParameters("String geocellsP");
//	    List<GeoLocation> objects = (List<GeoLocation>) query.execute(cells);

        // Just checking that cells are not empty
        Assert.assertTrue(cells.size() > 0);
        Assert.assertTrue(cells2.size() > 0);

        // Show in the log what cells shoud be used in the query
        log.log(Level.INFO, "Geocells to use in query for PointSW("+latSW+","+lonSW+") ; PointNE("+latNE+","+lonNE+") are: "+cells);
    }

    /**
     * To test proximity search, you have to give your base query and it will be enhanced with geocells restrictions.
     *
     */
    // TODO configure persistent manager to run a real test
    @Test
    public void testHowToQueryWithProximitySearch() {

        // other ideas https://stackoverflow.com/questions/45378268/search-10-nearest-locations-in-datastore
        Point center = new Point(40.68645125859434, -73.92800644040109);
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLatitude(center.getLat());
        geoLocation.setLongitude(center.getLon());
        geoLocation.setGeocells(GeocellManager.generateGeoCell(center));
        ofy().save().entity(geoLocation).now();

        center = new Point(40.6891334, -73.9849069);
        geoLocation = new GeoLocation();
        geoLocation.setLatitude(center.getLat());
        geoLocation.setLongitude(center.getLon());
        geoLocation.setGeocells(GeocellManager.generateGeoCell(center));
        ofy().save().entity(geoLocation).now();

        center = new Point(40.68888704954965, -73.98117855191231);
        geoLocation = new GeoLocation();
        geoLocation.setLatitude(center.getLat());
        geoLocation.setLongitude(center.getLon());
        geoLocation.setGeocells(GeocellManager.generateGeoCell(center));
        ofy().save().entity(geoLocation).now();

        List<GeoLocation> objects = GeocellManager.proximityFetch(center, 10, 1000, ofySearch);
        Assert.assertTrue(objects.size() == 2);

    }

}
