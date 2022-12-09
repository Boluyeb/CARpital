package uk.ac.tees.a0174604.carpital;

//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.Query;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoClass {

    @Insert

    void insertAllData(ReportModel model);

//    select all data
    @Query("select * from report")
    List<ReportModel> getAllData();


}
