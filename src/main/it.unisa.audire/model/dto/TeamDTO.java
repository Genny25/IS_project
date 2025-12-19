package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class TeamDTO implements Serializable {
    private int productionID;
    private int cdID;

    public TeamDTO() {}

    public TeamDTO(int productionID, int cdID) {
        this.productionID = productionID;
        this.cdID = cdID;
    }

    public int getProductionID() {
        return productionID;
    }

    public void setProductionID(int productionID) {
        this.productionID = productionID;
    }

    public int getCdID() {
        return cdID;
    }

    public void setCdID(int cdID) {
        this.cdID = cdID;
    }

    @Override
    public String toString() {
        return "TeamDTO{" +
                "productionID=" + productionID +
                ", cdID=" + cdID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TeamDTO teamDTO = (TeamDTO) o;
        return productionID == teamDTO.productionID && cdID == teamDTO.cdID;
    }
}
