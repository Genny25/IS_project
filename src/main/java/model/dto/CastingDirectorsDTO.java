package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class CastingDirectorsDTO implements Serializable {
    private int cdID;
    private int userID;

    public CastingDirectorsDTO() {
    }

    public CastingDirectorsDTO(int cdID, int userID) {
        this.cdID = cdID;
        this.userID = userID;
    }

    public int getCdID() {
        return cdID;
    }

    public void setCdID(int cdID) {
        this.cdID = cdID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "CastingDirectorsDTO{" +
                "cdID=" + cdID +
                ", userID=" + userID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CastingDirectorsDTO that = (CastingDirectorsDTO) o;
        return cdID == that.cdID && userID == that.userID;
    }
}
