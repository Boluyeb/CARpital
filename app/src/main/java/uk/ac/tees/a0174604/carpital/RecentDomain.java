//Handles recently added cars
package uk.ac.tees.a0174604.carpital;

public class RecentDomain {
    private String carModel;
    private String carMake;
    private String picture;
    private String cost;
    private String specs;

    public RecentDomain(String carMake, String carModel, String picture, String cost, String specs) {
        this.carModel = carModel;
        this.carMake = carMake;
        this.picture = picture;
        this.cost = cost;
        this.specs = specs;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carName) {
        this.carModel = carModel;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }
}
