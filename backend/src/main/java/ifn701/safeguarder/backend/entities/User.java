package ifn701.safeguarder.backend.entities;

public class User {

    private int id;
    private String fullName;
    private String email;
    private String password;
    private boolean activated;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean isAdmin) {
        this.admin = isAdmin;
    }

    private boolean admin;

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    private String profilePictureUrl;

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
    public void setId(int id) { this.id = id; }

    //FullName
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

}
