package uk.ac.tees.a0174604.carpital;

//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;
//import android.support.annotation.NonNull;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "report")
public class ReportModel {
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getCarManu() {
        return carManu;
    }

    public void setCarManu(String carManu) {
        this.carManu = carManu;
    }

    public String getCarTrim() {
        return carTrim;
    }

    public void setCarTrim(String carTrim) {
        this.carTrim = carTrim;
    }

    public String getCarTrans() {
        return carTrans;
    }

    public void setCarTrans(String carTrans) {
        this.carTrans = carTrans;
    }
//create table

//    primary key
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int key;

    @ColumnInfo(name = "vin")
    private String vin;

    @ColumnInfo(name = "carMake")
    private String carMake;

    @ColumnInfo(name = "carModel")
    private String carModel;

    @ColumnInfo(name = "carYear")
    private String carYear;

    @ColumnInfo(name = "carManu")
    private String carManu;

    @ColumnInfo(name = "carTrim")
    private String carTrim;

    @ColumnInfo(name = "carTrans")
    private String carTrans;

    public int getKey() {
        return key;
    }

    public void setKey(int key){
        this.key = key;
    }


}
