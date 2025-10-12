package model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ApplyDTO implements Serializable {
    private int performerID;
    private int castingID;
    private String feedback;
    private Status status;
    private LocalDateTime applicationDate;

    public enum Status {
        IN_ATTESA,
        SHORTLIST,
        SELEZIONATA,
        RIFIUTATA
    }

    public ApplyDTO() {
    }

    public ApplyDTO(int performerID, int castingID, String feedback, Status status, LocalDateTime applicationDate) {
        this.performerID = performerID;
        this.castingID = castingID;
        this.feedback = feedback;
        this.status = status;
        this.applicationDate = applicationDate;
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

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    @Override
    public String toString() {
        return "ApplyDTO{" +
                "performerID=" + performerID +
                ", castingID=" + castingID +
                ", feedback='" + feedback + '\'' +
                ", status=" + status +
                ", applicationDate=" + applicationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ApplyDTO applyDTO = (ApplyDTO) o;
        return performerID == applyDTO.performerID && castingID == applyDTO.castingID && Objects.equals(feedback, applyDTO.feedback) && status == applyDTO.status && Objects.equals(applicationDate, applyDTO.applicationDate);
    }
}
