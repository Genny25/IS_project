package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class ProductionsDTO implements Serializable {
    private int productionID;
    private String title;
    private String type;
    private int pmID;

    public ProductionsDTO() {
    }

    public ProductionsDTO(int productionID, String title, String type, int pmID) {
        this.productionID = productionID;
        this.title = title;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPmID() {
        return pmID;
    }

    public void setPmID(int pmID) {
        this.pmID = pmID;
    }

    @Override
    public String toString() {
        return "ProductionsDTO{" +
                "productionID=" + productionID +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", pmID=" + pmID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductionsDTO that = (ProductionsDTO) o;
        return productionID == that.productionID && pmID == that.pmID && Objects.equals(title, that.title) && Objects.equals(type, that.type);
    }
}
