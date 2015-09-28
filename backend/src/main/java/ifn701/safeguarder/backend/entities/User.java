package ifn701.safeguarder.backend.entities;

public class User {
    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean getActivated() {return activated; }
    public void setActivated(Boolean activated) { this.activated = activated; }

    private int id;
    private String fullName;
    private String email;
    private String password;
    private boolean activated;
}
