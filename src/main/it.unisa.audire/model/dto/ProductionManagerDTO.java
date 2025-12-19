package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class ProductionManagerDTO implements Serializable {
    private int pmID;
    private String userID;

    public ProductionManagerDTO() {}

    public ProductionManagerDTO(int pmID, String userID) {
        this.pmID = pmID;
        this.userID = userID;
    }

    public int getPmID() {
        return pmID;
    }

    public void setPmID(int pmID) {
        this.pmID = pmID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "ProductionManagerDTO{" +
                "pmID=" + pmID +
                ", userID='" + userID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductionManagerDTO that = (ProductionManagerDTO) o;
        return pmID == that.pmID && Objects.equals(userID, that.userID);
    }
}
