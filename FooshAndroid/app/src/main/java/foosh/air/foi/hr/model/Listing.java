package foosh.air.foi.hr.model;

import java.util.Date;
import java.util.List;

public class Listing {
    private List<String> category;
    private String dateCreated;
    private String description;
    private boolean hiring;
    private List<String> images;

    private String ownerId;

    private String qrCode;
    private String title;
    private String location;
    private int price;

    public Listing() {
    }

    public Listing(List<String> category, String dateCreated, String description, boolean hiring, List<String> images, String ownerId, String qrCode, String title, String location, int price) {
        this.category = category;
        this.dateCreated = dateCreated;
        this.description = description;
        this.hiring = hiring;
        this.images = images;
        this.ownerId = ownerId;
        this.qrCode = qrCode;
        this.title = title;
        this.location = location;
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHiring() {
        return hiring;
    }

    public void setHiring(boolean hiring) {
        this.hiring = hiring;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
