package foosh.air.foi.hr.model;

public class Interest{
    private String userId;
    private boolean requestFinal;
    private boolean Finalised;

    public Interest(String userId, boolean requestFinal, boolean finalised) {
        this.userId = userId;
        this.requestFinal = requestFinal;
        Finalised = finalised;
    }

    public Interest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRequestFinal() {
        return requestFinal;
    }

    public void setRequestFinal(boolean requestFinal) {
        this.requestFinal = requestFinal;
    }

    public boolean isFinalised() {
        return Finalised;
    }

    public void setFinalised(boolean finalised) {
        Finalised = finalised;
    }
}