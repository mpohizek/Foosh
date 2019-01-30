package foosh.air.foi.hr.model;

/**
 * Model klasa za korisnika
 */
public class User {
    private String displayName;
    private String profileImgPath;
    private String contact;
    private String bio;
    private String email;
    private String location;

    public User(String displayName, String profileImgPath, String contact, String bio, String email, String location) {
        this.displayName = displayName;
        this.profileImgPath = profileImgPath;
        this.contact = contact;
        this.bio = bio;
        this.email = email;
        this.location = location;
    }

    public User() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImgPath() {
        return profileImgPath;
    }

    public void setProfileImgPath(String profileImgPath) {
        this.profileImgPath = profileImgPath;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() { return contact; }

    public void setContact(String contact) { this.contact = contact; }
}
