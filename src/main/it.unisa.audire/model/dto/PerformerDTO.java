package model.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class PerformerDTO implements Serializable {
    private int performerID;
    private Gender gender;
    private Category category;
    private String description;
    private byte[] cvData;
    private String cvMimeType;
    private String profilePhoto;
    private int userID;

    public PerformerDTO() {}

    public PerformerDTO(int performerID, Gender gender, Category category, String description, byte[] cvData, String cvMimeType, String profilePhoto, int userID) {
        this.performerID = performerID;
        this.gender = gender;
        this.category = category;
        this.description = description;
        this.cvData = cvData;
        this.cvMimeType = cvMimeType;
        this.profilePhoto = profilePhoto;
        this.userID = userID;
    }

    public int getPerformerID() {
        return performerID;
    }

    public void setPerformerID(int performerID) {
        this.performerID = performerID;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCvData() {
        return cvData;
    }

    public void setCvData(byte[] cvData) {
        this.cvData = cvData;
    }

    public String getCvMimeType() {
        return cvMimeType;
    }

    public void setCvMimeType(String cvMimeType) {
        this.cvMimeType = cvMimeType;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "PerformerDTO{" +
                "performerID=" + performerID +
                ", gender=" + gender +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", cvData=" + Arrays.toString(cvData) +
                ", cvMimeType='" + cvMimeType + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", userID=" + userID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PerformerDTO that = (PerformerDTO) o;
        return performerID == that.performerID && userID == that.userID && gender == that.gender && category == that.category && Objects.equals(description, that.description) && Objects.deepEquals(cvData, that.cvData) && Objects.equals(cvMimeType, that.cvMimeType) && Objects.equals(profilePhoto, that.profilePhoto);
    }

    public enum Gender {
        M,
        F,
        Altro,
    }

    public enum Category {
        Attore_Attrice,
        Musicista,
        Cantante,
        Ballerino,
        Doppiatore_Trice,
        Qualsiasi,
    }

}
