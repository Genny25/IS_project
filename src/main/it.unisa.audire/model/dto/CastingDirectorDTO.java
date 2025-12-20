package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class CastingDirectorDTO implements Serializable {
    private int cdID;
    private int userID;

    public CastingDirectorDTO(){}

    public CastingDirectorDTO(int cdID, int userID) {
        this.cdID = cdID;
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCdID() {
        return cdID;
    }

    public void setCdID(int cdID) {
        this.cdID = cdID;
    }

    @Override
    public String toString() {
        return "CastingDirectorDTO{" +
                "cdID=" + cdID +
                ", userID='" + userID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CastingDirectorDTO that = (CastingDirectorDTO) o;
        return cdID == that.cdID &&  userID == that.userID;
    }
}
