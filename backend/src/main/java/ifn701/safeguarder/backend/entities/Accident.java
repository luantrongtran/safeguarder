package ifn701.safeguarder.backend.entities;

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

    public Blob getImage1() {
        return image1;
    }

    public void setImage1(Blob image1) {
        this.image1 = image1;
    }

    public Blob getImage2() {
        return image2;
    }

    public void setImage2(Blob image2) {
        this.image2 = image2;
    }

    public Blob getImage3() {
        return image3;
    }

    public void setImage3(Blob image3) {
        this.image3 = image3;
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
    private Blob image1;
    private Blob image2;
    private Blob image3;

}