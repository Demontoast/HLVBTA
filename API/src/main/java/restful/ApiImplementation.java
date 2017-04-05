package restful;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;


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

      //  if(lastLat==0&&lastLong==0)
        //    return Collections.emptyList();

        try (Connection conn = sql2o.open()) {
            List<AD> adINFO = conn.createQuery("SELECT " +
                    "t2.title,t2.description,t2.websiteUrl,t1.latitude,t1.longitude,t1.minDistance,t1.maxDistance " +
                    "FROM Location t1 " +
                    "inner join AD t2 " +
                    "on t1.adID = t2.adID " +
                    "where  (6371 * acos( cos( radians(:LATNum) ) * cos( radians( latitude ) ) * cos( radians( :LONGNum ) - radians(longitude) ) + sin( radians(:LATNum) ) * sin( radians(latitude) ) )) " +
                    "between minDistance and maxDistance " +
                    "and :speed between minSpeed and maxSpeed "+
                    "ORDER BY (6371 * acos( cos( radians(:LATNum) ) * cos( radians( latitude ) ) * cos( radians( :LONGNum ) - radians(longitude) ) + sin( radians(:LATNum) ) * sin( radians(latitude) ) )) ASC LIMIT 1"
            )
                    .addParameter("LATNum", latitude)
                    .addParameter("LONGNum", longitude)
                    .addParameter("speed", speed)
                    .executeAndFetch(AD.class);





            return adINFO;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

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
