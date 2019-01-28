package foosh.air.foi.hr.model;


import java.util.List;

public class User {
    private String displayName;
    private String profileImgPath;
    private String contact;
    private String bio;
    private String email;
    private String location;
    private float ratingHired;
    private float ratingEmployed;

    public User(String displayName, String profileImgPath, String contact, String bio, String email, String location, float ratingHired, float ratingEmployed) {
        this.displayName = displayName;
        this.profileImgPath = profileImgPath;
        this.contact = contact;
        this.bio = bio;
        this.email = email;
        this.location = location;
        this.ratingHired = ratingHired;
        this.ratingEmployed = ratingEmployed;
    }

    public User() {
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public float getRatingHired() {
        return ratingHired;
    }

    public void setRatingHired(float ratingHired) {
        this.ratingHired = ratingHired;
    }

    public float getRatingEmployed() {
        return ratingEmployed;
    }

    public void setRatingEmployed(float ratingEmployed) {
        this.ratingEmployed = ratingEmployed;
    }
}
