package model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class CastingsDTO implements Serializable {
    private int castingID;
    private String location;
    private String description;
    private String category;
    private LocalDateTime date;
    private LocalDateTime deadLine;
    private String title;
    private int cdID;
    private int productionID;

    public CastingsDTO() {
    }

    public CastingsDTO(int castingID, String location, String description, String category, LocalDateTime date, LocalDateTime deadLine, String title, int cdID, int productionID) {
        this.castingID = castingID;
        this.location = location;
        this.description = description;
        this.category = category;
        this.date = date;
        this.deadLine = deadLine;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine;
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
        return "Castings{" +
                "castingID=" + castingID +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", date=" + date +
                ", deadLine=" + deadLine +
                ", title='" + title + '\'' +
                ", cdID=" + cdID +
                ", productionID=" + productionID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CastingsDTO castings = (CastingsDTO) o;
        return castingID == castings.castingID && cdID == castings.cdID && productionID == castings.productionID && Objects.equals(location, castings.location) && Objects.equals(description, castings.description) && Objects.equals(category, castings.category) && Objects.equals(date, castings.date) && Objects.equals(deadLine, castings.deadLine) && Objects.equals(title, castings.title);
    }
}
