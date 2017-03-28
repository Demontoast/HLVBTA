package restful;


import java.util.List;

/**
 * Copyright Dr. Ganesh R. Baliga
 * All rights reserved.
 */

/**
    Abstract class specifying the functionality provided by the restful api
 */
public abstract class Api {

    static Api theApi = null;

    public static Api getApi() {
        if (theApi == null)
            theApi = new ApiImplementation();
        return theApi;
    }


    /**
     * Returns all cities in the given country
     * @param country The country
     * @return list of cities in the country
     */
    public abstract List<Location> getLocation(float latitude, float longitude);
    public abstract List<AD> getAD(float latitude, float longitude, float speed);
    public abstract List<City> getCities(String country);



    /**
     * Add a city to a country
     * @param name  name of city
     * @param countryName name of country
     * @param district  district that the city is located in
     * @param population population of the city
     * @return true if the city was added, false otherwise
     */

    public abstract boolean createCity (
            String name,
            String countryName,
            String district,
            int population
    );

    public abstract boolean createLocations (
            float latitude,
            float longitude,
            float minDist,
            float maxDist
    );

}
