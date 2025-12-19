package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class CastingDirectorDTO implements Serializable {
    private int cdID;
    private String userID;

    public CastingDirectorDTO(){}

    public CastingDirectorDTO(int cdID, String userID) {
        this.cdID = cdID;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
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
        return cdID == that.cdID && Objects.equals(userID, that.userID);
    }
}
