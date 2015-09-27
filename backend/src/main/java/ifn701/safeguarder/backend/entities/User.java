package ifn701.safeguarder.backend.entities;

public class User {

    private int id;
    private String fullName;
    private String email;
    private String password;
    private boolean activated;

    private UserSetting userSetting;

    public UserSetting getUserSetting() {
        return userSetting;
    }

    public void setUserSetting(UserSetting userSetting) {
        this.userSetting = userSetting;
    }

    //ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //FullName
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    //Email
    public String getEmail() { return email;}

    public void setEmail(String email){
        this.email = email;
    }

    //Password
    public String getPassword() { return password;}

    public void setPassword(String password){
        this.password = password;
    }

    //Activated
    public boolean getActivated() { return activated;}

    public void setActivated(boolean activated){
        this.activated = activated;
    }

}
