package restful;

/**
 * Location contains:
 *
 */
public class Location {

    float latitude;
    float longitude;
    float minDistance;
    float maxDistance;

    public Location(){

    }

    public float getLat() {
        return latitude;
    }
    public float getLong() { return longitude; }
    public float getMinDistance() {
        return minDistance;
    }
    public float getMaxDistance() {
        return maxDistance;
    }

    public void setLat(float x) {
        this.latitude = x;
    }
    public void setLong(float x) {
        this.longitude = x;
    }
    public void setMinDistance(float x) {
        this.minDistance = x;
    }
    public void setMaxDistance(float x) {
        this.maxDistance = x;
    }



}
