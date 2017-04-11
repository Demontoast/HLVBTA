package restful;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    public List<AD> getAD(float latitude, float longitude, float speed, float lastLat, float lastLong){

        if(lastLat == 0f && lastLong == 0f)
            return Collections.emptyList();

        try (Connection conn = sql2o.open()) {
            List<AD> adINFO = conn.createQuery(
                    "SELECT t2.adID, t2.title,t2.description,t2.websiteUrl,t1.latitude,t1.longitude,t1.minDistance,t1.maxDistance "+
                            "FROM Location t1 "+
                            "inner join AD t2 "+
                            "on t1.adID = t2.adID "+
                            "where ((6371 * acos( cos( radians(:LATNum) ) * cos( radians( latitude ) ) * cos( radians( :LONGNum ) - radians(longitude) ) + sin( radians(:LATNum) ) * sin( radians(latitude) ) )) "+
                            "between minDistance and maxDistance "+
                            "and :speed between minSpeed and maxSpeed) "+
                            "and ((t2.Direction = 1 and (((6371 * acos( cos( radians(:lastLat) ) * cos( radians( latitude ) ) * cos( radians(:lastLong) - radians(longitude) ) + sin( radians(:lastLat) ) * sin( radians(latitude) ) )))-((6371 * acos( cos( radians(:LATNum) ) * cos( radians( latitude ) ) * cos( radians( :LONGNum ) - radians(longitude) ) + sin( radians(:LATNum) ) * sin( radians(latitude) ) ))))> 0) " +
                            "OR (t2.Direction = 0 and (((6371 * acos( cos( radians(:lastLat) ) * cos( radians( latitude ) ) * cos( radians(:lastLong) - radians(longitude) ) + sin( radians(:lastLat) ) * sin( radians(latitude) ) )))-((6371 * acos( cos( radians(:LATNum) ) * cos( radians( latitude ) ) * cos( radians( :LONGNum ) - radians(longitude) ) + sin( radians(:LATNum) ) * sin( radians(latitude) ) ))))<0) " +
                            "OR (t2.Direction = 2)) "+
                            "ORDER BY (6371 * acos( cos( radians(:LATNum) ) * cos( radians( latitude ) ) * cos( radians( :LONGNum ) - radians(longitude) ) + sin( radians(:LATNum) ) * sin( radians(latitude) ) )) ASC LIMIT 1;"
            )
                    .addParameter("LATNum", latitude)
                    .addParameter("LONGNum", longitude)
                    .addParameter("speed", speed)
                    .addParameter("lastLat", lastLat)
                    .addParameter("lastLong", lastLong)
                    .executeAndFetch(AD.class);

            return adINFO;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }


    //GetDevID gets the devID fromt he ap which is displaying the current ad. For every ad that the ap displays, the assoiated devID will have its number of ads shown increase by 1.
    //IF a new devId is given create a new entry in the table, this way the devIDs are separate and we can tell which developer is showing more ads.
    // this method may not necessary
    public void getDevID(int devID){
        try (Connection conn = sql2o.open()){

            //create the query to select the devID and its views and incrementing it by 1.
            conn.createQuery("UPDATE Developer "
                        + "SET impressions = impressions + 1"
                        + " WHERE devID = :dev;"
                        )
                    .addParameter("dev", devID)
                    .executeUpdate();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

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
     * Note: Currently unfinished. Need to implement MySQL code to pull from DB.
     * Commented out code for the time being and forced a return true value. Will update when it works.
     *  - Michael Marchina
     ********************/
    public boolean checkDirection(float latitude, float longitude) {
        return true; // DEBUG: Returns true because I know this doesn't work yet, if we merge I don't want it to break the program.
        /*File file = new File("location.txt");

        FileWriter fw = null;
        BufferedWriter writer = null;

        if !(file.exists()) { // File does not exist, create it and store location values.
            try {
                fr = new FileWriter(file)
                writer = new BufferedWriter(fw);
                writer.write(latitude); // Store latitude on line 0.
                writer.newLine();
                writer.write(longitude); // Store longitude on line 1.
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fr.close();
                writer.close();
            }
            System.out.println("Loading..."); // Show that we are "loading" as in we are waiting for a comparison.
            return false;
        } else {

             File does exist, so we will compare current location to the ad with previous, if
             the distance is larger, we will return false, if it is smaller, we will return true.


            FileReader fr = null;
            BufferedReader reader = null;

            float prevLat = 0;
            float prevLong = 0;

            try {
                fr = new FileReader(file);
                reader = new BufferedReader(fr);
                prevLat = Float.valueOf(reader.readLine()); // Reads latitude from line 0 and stores it.
                prevLong = Float.valueOf(reader.readLine()); // Reads longitude from line 1 and stores it.
                return true; // DEBUG: Returns true if we successfully read from the file and stored prevLat and prevLong
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fr.close();
                reader.close();
            }

            /* Now we will read from the MySQL server to compare the distance from the previous against
               the current. After comparing we will write the current lat/long to the file.

            // TODO: Implement MySQL server read
            return false;
        }
        }
        */
    }

/*    @Override
    public List<Location> getLocation(float latitude, float longitude, float minDist, float maxDist) {
        try (Connection conn = sql2o.open()) {
            List<Location> locations;
            locations = conn.createQuery("select Location.latitude, " + "Location.longitude," +
                            "Location.minDistance, " +
                            "Location.maxDistance, " +
                            "(6371 * acos( cos( radians(lat) ) * cos( radians( Location.latitude ) ) * cos( radians(long) - radians(Location.longitude) ) + sin( radians(lat) ) * sin( radians(Location.latitude) ) )) AS distance "+
                            "where distance between minDist and maxDist"
                    )
                    .addParameter("lat", latitude)
                    .addParameter("long", longitude)
                    .addParameter("minDist", latitude)
                    .addParameter("maxDist", longitude)
                    .executeAndFetch(Location.class);
            return locations;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }
*/

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