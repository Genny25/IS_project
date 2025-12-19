package model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ApplicationDTO implements Serializable {

    private int applicationID;
    private LocalDateTime sendingDate;
    private Status status;
    private String feedback;
    private int performerID;
    private int castingID;

    public ApplicationDTO() {}

    public ApplicationDTO(int applicationID, LocalDateTime sendingDate, Status status, String feedback, int performerID, int castingID) {
        this.applicationID = applicationID;
        this.sendingDate = sendingDate;
        this.status =  status;
        this.feedback = feedback;
        this.performerID = performerID;
        this.castingID = castingID;
    }

    public int getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public LocalDateTime getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(LocalDateTime sendingDate) {
        this.sendingDate = sendingDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getPerformerID() {
        return performerID;
    }

    public void setPerformerID(int performerID) {
        this.performerID = performerID;
    }

    public int getCastingID() {
        return castingID;
    }

    public void setCastingID(int castingID) {
        this.castingID = castingID;
    }

    @Override
    public String toString() {
        return "ApplicationDTO{" +
                "applicationID=" + applicationID +
                ", sendingDate=" + sendingDate +
                ", status=" + status +
                ", feedback='" + feedback + '\'' +
                ", performerID=" + performerID +
                ", castingID=" + castingID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationDTO that = (ApplicationDTO) o;
        return applicationID == that.applicationID && performerID == that.performerID && castingID == that.castingID && Objects.equals(sendingDate, that.sendingDate) && status == that.status && Objects.equals(feedback, that.feedback);
    }

    public enum Status {
        In_attesa,
        Shortlist,
        Selezionata,
        Rifiutata,
    }
}
