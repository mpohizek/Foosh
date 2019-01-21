package foosh.air.foi.hr.model.ffunction;

public class MyListings {
    String ownerId;
    boolean hiring;
    int startAt;
    int limit;

    public MyListings(String ownerId, boolean hiring, int startAt, int limit) {
        this.ownerId = ownerId;
        this.hiring = hiring;
        this.startAt = startAt;
        this.limit = limit;
    }

    public MyListings() {
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isHiring() {
        return hiring;
    }

    public void setHiring(boolean hiring) {
        this.hiring = hiring;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
