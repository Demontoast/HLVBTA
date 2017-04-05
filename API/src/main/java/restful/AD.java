package restful;

/**
 * Created by Jacob_Kershaw on 3/26/17.
 */
public class AD {

    int adID;
    String title;
    String description;
    int impressions;
    int uniqueUsrs;
    int maxSpeed;
    int minSpeed;

    float latitude;
    float longitude;
    float minDistance;
    float maxDistance;

    int devID;
    float moneyOwned;
    int totalAdViews;

    String websiteUrl;

    public AD(){

    }

    public int getAdID() {
        return adID;
    }
    public String getTitle() { return title; }
    public String getDesc() {
        return description;
    }
    public int getImpressions() {
        return impressions;
    }
    public int getUniqueUsr() {
        return uniqueUsrs;
    }
    public int getMaxSpeed() {
        return maxSpeed;
    }
    public int getMinSpeed() {
        return minSpeed;
    }
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setAdID(int x) {
        this.adID = x;
    }
    public void setTitle(String x) {
        this.title = x;
    }
    public void setDesc(String x) {
        this.description = x;
    }
    public void setImpressions(int x) {
        this.impressions = x;
    }
    public void setUniqueUsr(int x) {
        this.uniqueUsrs = x;
    }
    public void setMaxSpeed(int x) {
        this.maxSpeed = x;
    }
    public void setMinSpeed(int x) {
        this.minSpeed = x;
    }
    public void setWebsiteUrl(String x) {
        this.websiteUrl = x;
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

    public int getDevID() {
        return devID;
    }
    public float getMoneyOwned() { return moneyOwned; }
    public int getTotalAdViews() {
        return totalAdViews;
    }

    public void setDevID(int x) {
        this.devID = x;
    }
    public void setMoneyOwned(float x) {
        this.moneyOwned = x;
    }
    public void setTotalAdViews(int x) { this.totalAdViews = x; }


}
