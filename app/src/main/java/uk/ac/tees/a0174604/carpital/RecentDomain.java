//Handles recently added cars
package uk.ac.tees.a0174604.carpital;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

//@IgnoreExtraProperties
public class RecentDomain {

    private String carMake;
    private String picture;
    private String year;
    private String cost;
    private String cate;
    private String location;
    private String carModel;

    public RecentDomain(){

    }

    public RecentDomain(String carMake, String carModel, String year, String cate, String location, String cost, String picture) {

        this.carMake = carMake;
        this.carModel = carModel;
        this.year = year;
        this.cate = cate;
        this.location = location;
        this.cost = cost;
        this.picture = picture;

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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
