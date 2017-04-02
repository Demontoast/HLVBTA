package restful;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright Dr. Ganesh R. Baliga
 * All rights reserved.
 */
public class ApiImplementation extends Api {

    Logger logger = LoggerFactory.getLogger(ApiImplementation.class);

    Sql2o sql2o;
    String db_jdbc_url, db_user, db_password;

    //final String DBInfoFile = "DB_INFO.txt"; // Text file containing db credentials

    // Read database connection information from credentials file
    // Credentials file format:
    // DB_JDBC_URL=jdbc:mysql://your_msql_dbserver.com/your_database
    // DB_USER=your_db_username
    // DB_PASSWORD=your_db_password

    boolean init() {
        db_jdbc_url = "jdbc:mysql://127.0.0.1:3306/HLVBTA?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST";
        db_user = "root";
        db_password = "moxie77";
        return true;

    }

    public ApiImplementation() {

        if (!init()) {
            System.err.println("Unable to read database information");
            System.exit(0);
        }

        sql2o = new Sql2o(db_jdbc_url, db_user, db_password);

    }


    @Override
    public List<City> getCities(String country) {
        try (Connection conn = sql2o.open()) {
            List<City> cities =
                    conn.createQuery("select city.name, city.population from city, country where "
                            + "city.countrycode=country.code and country.name=:countryName "
                            + "order by city.population desc;")
                            .addParameter("countryName", country)
                            .executeAndFetch(City.class);
            return cities;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }
    @Override
    public List<AD> getAD(float latitude, float longitude, float speed){
        try (Connection conn = sql2o.open()) {
            if (checkDirection(latitude, longitude, speed)) {
                List<AD> adINFO = conn.createQuery("SELECT " +
                        "t2.*,t1.latitude,t1.longitude,t1.minDistance,t1.maxDistance,t3.moneyOwned,t3.totalAdViews " +
                        "FROM Location t1 " +
                        "inner join AD t2 " +
                        "on t1.adID = t2.adID " +
                        "inner join Developer t3 " +
                        "on t2.devID = t3.devID " +
                        "where  (6371 * acos( cos( radians(:LATNum) ) * cos( radians( latitude ) ) * cos( radians( :LONGNum ) - radians(longitude) ) + sin( radians(:LATNum) ) * sin( radians(latitude) ) )) " +
                        "between minDistance and maxDistance " +
                        "and :speed between minSpeed and maxSpeed"
                )
                        .addParameter("LATNum", latitude)
                        .addParameter("LONGNum", longitude)
                        .addParameter("speed", speed)
                        .executeAndFetch(AD.class);
                return adINFO;
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    /********************
     * Check the direction of where we're going via a simple boolean
     * We will get the user's current lat/long, save it as a previous locally
     * This will return false and print "Loading..."
     *
     * On the next update, we will pull from the local file and compare the new lat/long.
     * If it is moving towards the lat/long of the ads, it will return true.
     * If not, it will return false.
     *
     * Note: Current does not work, and simply returns true.
     *  - Michael Marchina
     ********************/
    public boolean checkDirection(float latitude, float longitude, float speed) {
        return true;
    }

 //   @Override
  //  public List<Location> getLocation(float latitude, float longitude, float minDist, float maxDist) {
  //      try (Connection conn = sql2o.open()) {
   //         List<Location> locations;
    //        locations = conn.createQuery("select Location.latitude, " + "Location.longitude," +
      //                      "Location.minDistance, " +
        //                    "Location.maxDistance, " +
          //                  "(6371 * acos( cos( radians(lat) ) * cos( radians( Location.latitude ) ) * cos( radians(long) - radians(Location.longitude) ) + sin( radians(lat) ) * sin( radians(Location.latitude) ) )) AS distance "+
            //                "where distance between minDist and maxDist"
              //      )
                //    .addParameter("lat", latitude)
                 //   .addParameter("long", longitude)
                  //  .addParameter("minDist", latitude)
//                    .addParameter("maxDist", longitude)
  //                  .executeAndFetch(Location.class);
    //        return locations;
      //  }
//        catch (Exception e) {
  //          logger.error(e.getMessage());
    //    }
//
  //      return null;
    //}

    @Override //createQuery -> NOT WORKING
    public List<Location> getLocation(float latitude, float longitude) {
        try (Connection conn = sql2o.open()) {
            List<Location> locations = conn.createQuery("select latitude,longitude,minDistance,maxDistance from Location " +
                    "where (6371 * acos( cos( radians( :LATNum ) ) * cos( radians( latitude ) ) * cos( radians( :LONGNum ) - radians( longitude ) ) + sin( radians( :LATNum ) ) * sin( radians( latitude ) ) )) " +
                    "between minDistance and maxDistance"
                )
                    .addParameter("LATNum", latitude)
                    .addParameter("LONGNum", longitude)
                    .executeAndFetch(Location.class);
            return locations;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }


    @Override
    public boolean createCity (
            String name,
            String countryName,
            String district,
            int population
    ) {
        try (Connection conn = sql2o.open()) {
            List<String> codes =
                    conn.createQuery("select code from country where name=:countryName")
                            .addParameter("countryName", countryName)
                            .executeAndFetch(String.class);
            if (codes.size() != 1) {
                return false;
            }
            else {
                String code = codes.get(0);
                conn.createQuery("insert into city (name, countrycode, district, population) "
                        + "values (:name, :code, :district, :population);")
                        .addParameter("name", name)
                        .addParameter("code", code)
                        .addParameter("district", district)
                        .addParameter("population", population)
                        .executeUpdate();
                return true;

            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createLocations (
            float latitude,
            float longitude,
            float minDist,
            float maxDist
    ) {
        try (Connection conn = sql2o.open()) {
            List<Location> locations;
            locations = conn.createQuery("select latitude,longitude,minDistance,maxDistance from Location"
            )
                    // .addParameter("lat", latitude)
                    // .addParameter("long", longitude)
                    .executeAndFetch(Location.class);
            if(locations.size()==0)
                return false;
            return true;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }


}
