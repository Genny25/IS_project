package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class ProductionManagersDTO implements Serializable {
    private int pmID;
    private int userID;

    public ProductionManagersDTO() {
    }

    public ProductionManagersDTO(int pmID, int userID) {
        this.pmID = pmID;
        this.userID = userID;
    }

    public int getPmID() {
        return pmID;
    }

    public void setPmID(int pmID) {
        this.pmID = pmID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "ProductionManagersDTO{" +
                "pmID=" + pmID +
                ", userID=" + userID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductionManagersDTO that = (ProductionManagersDTO) o;
        return pmID == that.pmID && userID == that.userID;
    }
}
