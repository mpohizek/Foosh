package foosh.air.foi.hr.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Model klasa za oglas
 */
public class Listing {
    private String category;
    private String dateCreated;
    private String description;
    private boolean hiring;
    private boolean active;
    private String id;
    private ArrayList<String> images = new ArrayList<>();
    private String location;
    private String ownerId;
    private int price;
    private String qrCode;
    private String status;
    private String title;
    private int orderNum;
    private HashMap<String, String> applications = new HashMap<String, String>();
    private HashMap<String, Integer> applicant = new HashMap<String, Integer>();

    public Listing() {
    }

    public Listing(String category, String dateCreated,
                   String description, boolean hiring, boolean active, String id,
                   ArrayList<String> images, String location, String ownerId, int price,
                   String qrCode, String status, String title, int orderNum, HashMap<String, String> applications,
                   HashMap<String, Integer> applicant) {
        this.category = category;
        this.dateCreated = dateCreated;
        this.description = description;
        this.hiring = hiring;
        this.active = active;
        this.id = id;
        this.images = images;
        this.location = location;
        this.ownerId = ownerId;
        this.price = price;
        this.qrCode = qrCode;
        this.status = status;
        this.title = title;
        this.orderNum = orderNum;
        this.applications = applications;
        this.applicant = applicant;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateCreated() {
        return dateCreated;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public HashMap<String, String> getApplications() {
        return applications;
    }

    public void setApplications(HashMap<String, String> applications) { this.applications = applications; }

    public HashMap<String, Integer> getApplicant() {
        return applicant;
    }

    public void setApplicant(HashMap<String, Integer> applicant) {
        this.applicant = applicant;
    }

    public int getOrderNum() { return orderNum; }

    public void setOrderNum(int orderNum) { this.orderNum = orderNum; }
}
