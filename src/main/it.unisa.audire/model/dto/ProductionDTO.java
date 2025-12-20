package model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ProductionDTO implements Serializable {
    private int productionID;
    private String title;
    private Type type;
    private LocalDateTime creationDate;
    private int pmID;

    public ProductionDTO() {}

    public ProductionDTO(int productionID, String title, Type type, LocalDateTime creationDate, int pmID) {
        this.productionID = productionID;
        this.title = title;
        this.type = type;
        this.creationDate = creationDate;
        this.pmID = pmID;
    }

    public int getProductionID() {
        return productionID;
    }

    public void setProductionID(int productionID) {
        this.productionID = productionID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getPmID() {
        return pmID;
    }

    public void setPmID(int pmID) {
        this.pmID = pmID;
    }

    @Override
    public String toString() {
        return "ProductionDTO{" +
                "productionID=" + productionID +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", creationDate=" + creationDate +
                ", pmID=" + pmID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductionDTO that = (ProductionDTO) o;
        return productionID == that.productionID && pmID == that.pmID && Objects.equals(title, that.title) && type == that.type && Objects.equals(creationDate, that.creationDate);
    }

    public enum Type {
        Serie_TV,
        Film,
        Teatro,
        Musical,
        Pubblicità,
        Documentario,
        Cortometraggio,
        Web_Series,
        Altro
    }
}
