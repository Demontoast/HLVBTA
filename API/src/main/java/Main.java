/**
 * Created by Dr. Baliga on 2/11/17.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restful.Api;
import restful.Location;
import restful.AD;

import java.util.List;

import static spark.Spark.*;


public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {

        // Serve from port 8080 (instead of 4567)

        port(8080);
        // Location of the static html files
        staticFiles.location("/public");

        // Uncomment below line if webserver is different from
        // the server hosting the restapi. Refer CORS (cross origin resource sharing)

        //after(new CorsEnabler());

        get("/hello", (req, res) -> {
            logger.debug("Get request: /hello");
            return "Hello World";
        });

        get("/locations/:latitude/:longitude/:speed/:lastLat/:lastLong", (req, res) -> {
            float latitude = Float.parseFloat(req.params("latitude"));
            float longitude = Float.parseFloat(req.params("longitude"));
            float speed = Float.parseFloat(req.params("speed"));
            float lastLat = Float.parseFloat(req.params("lastLat"));
            float lastLong = Float.parseFloat(req.params("lastLong"));

            Api myapi = Api.getApi();
            List<AD> ads = myapi.getAD(latitude, longitude, speed, lastLat,lastLong);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            return gson.toJson(ads);
        });

        get("/dev/:devID", (req, res) -> {
            int devID = Integer.parseInt(req.params("devID"));

            Api myapi = Api.getApi();
            myapi.getDevID(devID);

            return "Worked";
        });


        post("/Location", (request, response) -> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            response.type("application/json");

            try {
                JsonObject obj = new JsonParser().parse(request.body()).getAsJsonObject();

                float latitude = obj.get("latitude").getAsFloat();
                float longitude = obj.get("longitude").getAsFloat();
                float minDist = obj.get("minDistance").getAsFloat();
                float maxDist = obj.get("maxDistance").getAsFloat();

                Api myapi = Api.getApi();

                if (myapi.createLocations(latitude,longitude,minDist,maxDist)) {
                    response.status(200);
                    return gson.toJson(obj);
                }
                else {
                    response.status(400);
                    return(null);
                }

            } catch (Exception e) {
                response.status(404);
                return (gson.toJson(e));
            }

        });



    }
}