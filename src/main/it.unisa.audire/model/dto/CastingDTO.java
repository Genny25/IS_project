package model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import model.dto.PerformerDTO.Category;

public class CastingDTO implements Serializable {

    private int castingID;
    private String location;
    private Category category;
    private String description;
    private LocalDateTime publishDate;
    private LocalDateTime deadline;
    private String title;
    private int cdID;
    private int productionID;

    public CastingDTO() {}

    public CastingDTO(int castingID, String location, Category category, String description, LocalDateTime publishDate, LocalDateTime deadline, String title, int cdID, int productionID) {
        this.castingID = castingID;
        this.location = location;
        this.category = category;
        this.description = description;
        this.publishDate = publishDate;
        this.deadline = deadline;
        this.title = title;
        this.cdID = cdID;
        this.productionID = productionID;
    }

    public int getCastingID() {
        return castingID;
    }

    public void setCastingID(int castingID) {
        this.castingID = castingID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCdID() {
        return cdID;
    }

    public void setCdID(int cdID) {
        this.cdID = cdID;
    }

    public int getProductionID() {
        return productionID;
    }

    public void setProductionID(int productionID) {
        this.productionID = productionID;
    }

    @Override
    public String toString() {
        return "CastingDTO{" +
                "castingID=" + castingID +
                ", location='" + location + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", publishDate=" + publishDate +
                ", deadline=" + deadline +
                ", title='" + title + '\'' +
                ", cdID=" + cdID +
                ", productionID=" + productionID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CastingDTO that = (CastingDTO) o;
        return castingID == that.castingID && cdID == that.cdID && productionID == that.productionID && Objects.equals(location, that.location) && category == that.category && Objects.equals(description, that.description) && Objects.equals(publishDate, that.publishDate) && Objects.equals(deadline, that.deadline) && Objects.equals(title, that.title);
    }
}
