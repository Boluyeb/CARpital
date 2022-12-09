package uk.ac.tees.a0174604.carpital;

//import android.arch.persistence.room.Database;
//import android.arch.persistence.room.Room;
//import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ReportModel.class}, version = 1)
public abstract class ReportDatabaseClass extends RoomDatabase {

    public abstract DaoClass getDao();
    private static ReportDatabaseClass instance;

    static ReportDatabaseClass getDatabase(final Context context){
        if(instance == null){
            synchronized (ReportDatabaseClass.class){
                instance = Room.databaseBuilder(context, ReportDatabaseClass.class, "DATABASE").allowMainThreadQueries().build();
            }
        }
        return instance;
    }

}
