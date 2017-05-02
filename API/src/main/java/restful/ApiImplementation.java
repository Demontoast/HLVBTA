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
    public List<AD> getAD(float latitude, float longitude, float speed, float lastLat, float lastLong, float time){

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
                            "and :time between startTime and endTime "+
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
                    .addParameter("time", time)
                    .executeAndFetch(AD.class);

            return adINFO;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }


    /********************
     * GetDevID gets the devID from the api which is displaying the current ad. For every ad that the ap displays, the assoiated devID will have its number of ads shown increase by 1.
     * IF a new devId is given create a new entry in the table, this way the devIDs are separate and we can tell which developer is showing more ads.
     * this method may not necessary
     ********************/
    public void getDevID(int devID){
        try (Connection conn = sql2o.open()){

            List<AD> ad = conn.createQuery("Select devID From AD WHERE adid = :adID;")
                    .addParameter("adID", devID)
                    .executeAndFetch(AD.class);

            int dev = ad.get(0).getDevID();
            //create the query to select the devID and its views and incrementing it by 1.
            conn.createQuery("UPDATE Developer "
                        + "SET click = click + 1"
                        + " WHERE devID = :dev;"
                        )
                    .addParameter("dev", devID)
                    .executeUpdate();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

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