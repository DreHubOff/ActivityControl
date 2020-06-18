package com.studying.activitycontrol.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ActivityEntity::class], version = 1, exportSchema = false)
abstract class DataBase : RoomDatabase() {
    abstract fun getActivityDao(): ActivityEntityDao

    companion object {
        fun getInstance(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DataBase::class.java,
            "activity_control"
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}