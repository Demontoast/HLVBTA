import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * Copyright Dr. Ganesh R. Baliga
 * All rights reserved.
 */

public class CorsEnabler implements Filter {
    @Override
    public void handle(Request request, Response response) throws Exception {
        response.header("Access-Control-Allow-Origin", "*");
        response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin," );
        response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS" );
        response.header("Access-Control-Allow-Credentials", "true");
    }
}
