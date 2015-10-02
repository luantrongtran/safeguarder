package ifn701.safeguarder.backend.entities;

import org.json.JSONObject;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class Accident {
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int id) {
        this.userId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getObservation_level() {
        return observation_level;
    }

    public void setObservation_level(int observation_level) {
        this.observation_level = observation_level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private int id;
    private int userId;
    private String name;
    private String type;
    private long time;
    private double lat;
    private double lon;
    private int observation_level;
    private String description;
    private String image1;
    private String image2;
    private String image3;

    User user;

    public JSONObject toJSon() {
        JSONObject jObj = new JSONObject();

        jObj.put("id", id);
        jObj.put("userId", userId);
        jObj.put("name", name);
        jObj.put("observation_level", observation_level);
        jObj.put("lat", lat);
        jObj.put("lon", lon);

        return jObj;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(!(obj instanceof Accident)) {
            return false;
        }
        Accident accident = (Accident)obj;
        if(accident.getId() == this.getId()) {
            return true;
        }
        return false;
    }
}