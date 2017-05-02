package restful;


import java.util.List;

/**
 * Created by Jacob_Kershaw on 3/26/17.
 */

public abstract class Api {

    static Api theApi = null;

    public static Api getApi() {
        if (theApi == null)
            theApi = new ApiImplementation();
        return theApi;
    }

    public abstract List<Location> getLocation(float latitude, float longitude);
    public abstract List<AD> getAD(float latitude, float longitude, float speed, float lastLat, float lastLong, float time);
    public abstract void getDevID(int devID);

    public abstract boolean createLocations (
            float latitude,
            float longitude,
            float minDist,
            float maxDist
    );

}
