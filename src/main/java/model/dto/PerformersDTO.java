package model.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class PerformersDTO implements Serializable {
    private int performerID;
    private String phoneNumber;
    private Gender gender;
    private String category;
    private String description;
    private byte[] cvData;
    private String cvMimeType;

    public enum Gender {
        M,
        F,
        Other,
        PreferNotToSay
    }

    public PerformersDTO() {
    }

    public PerformersDTO(int performerID, String phoneNumber, Gender gender, String category, String description, byte[] cvData, String cvMimeType) {
        this.performerID = performerID;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.category = category;
        this.description = description;
        this.cvData = cvData;
        this.cvMimeType = cvMimeType;
    }

    public int getPerformerID() {
        return performerID;
    }

    public void setPerformerID(int performerID) {
        this.performerID = performerID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    @Override
    public String toString() {
        return "PerformerDTO{" +
                "performerID=" + performerID +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", cvData=" + (cvData != null ? cvData.length + " bytes" : "null") +
                ", cvMimeType='" + cvMimeType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PerformersDTO that = (PerformersDTO) o;
        return performerID == that.performerID &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                gender == that.gender &&
                Objects.equals(category, that.category) &&
                Objects.equals(description, that.description) &&
                Arrays.equals(cvData, that.cvData) &&
                Objects.equals(cvMimeType, that.cvMimeType);
    }
}