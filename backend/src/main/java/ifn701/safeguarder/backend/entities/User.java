package ifn701.safeguarder.backend.entities;

/**
 * Created by lua on 15/08/2015.
 */
public class User {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private int id;
    private String fullName;
}
