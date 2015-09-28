package ifn701.safeguarder.backend.entities;

/**
 * Created by lua on 26/09/2015.
 */
public class UserSetting {
    private int userId;

    private double homeLocationLat;
    private double homeLocationLon;
    private String homeAddress;

    private float radius;

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public double getHomeLocationLat() {
        return homeLocationLat;
    }

    public void setHomeLocationLat(double homeLocationLat) {
        this.homeLocationLat = homeLocationLat;
    }

    public double getHomeLocationLon() {
        return homeLocationLon;
    }

    public void setHomeLocationLon(double homeLocationLon) {
        this.homeLocationLon = homeLocationLon;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
