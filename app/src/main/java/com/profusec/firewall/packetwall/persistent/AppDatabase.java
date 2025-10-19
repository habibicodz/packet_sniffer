package com.profusec.firewall.packetwall.persistent;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.profusec.firewall.packetwall.R;
import com.profusec.firewall.packetwall.model.AppFilter;
import com.profusec.firewall.packetwall.persistent.dao.AppFilterDao;

@Database(version = 2, entities = {AppFilter.class}, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // Instance
    private static AppDatabase appDatabase;

    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, context.getApplicationContext().getString(R.string.database_name)).enableMultiInstanceInvalidation().fallbackToDestructiveMigration(true).build();
        }

        return appDatabase;
    }

    public abstract AppFilterDao appFilterDao();
}