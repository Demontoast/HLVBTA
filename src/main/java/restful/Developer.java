package restful;

/**
 * Created by Jacob_Kershaw on 3/26/17.
 */
public class Developer {

    int devID;
    float moneyOwned;
    int totalAdViews;

    public Developer(){

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
