package foosh.air.foi.hr.model;

public class Category {
    private String name;
    private Integer listingCounter;

    public Category(){

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getListingCounter() { return  listingCounter;}
    public void setListingCounter(Integer listingCounter) {this.listingCounter=listingCounter;}
}
