package foosh.air.foi.hr.model;


import java.util.List;

public class User {
    private String displayName;
    private String profileImgUrl;
    private List<String> contact;
    private String bio;



    private String email;

    public User() {
    }

    public User(String displayName, String profileImgUrl, List<String> contact, String bio, String email) {
        this.displayName = displayName;
        this.profileImgUrl = profileImgUrl;
        this.contact = contact;
        this.bio = bio;
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImageUrl() {
        return profileImgUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.profileImgUrl = imageUrl;
    }

    public List<String> getContact() {
        return contact;
    }

    public void setContact(List<String> contact) {
        this.contact = contact;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
