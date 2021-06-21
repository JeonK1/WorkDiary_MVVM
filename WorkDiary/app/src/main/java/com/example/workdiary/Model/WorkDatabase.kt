package com.example.workdiary.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Work::class], version = 1)
abstract class WorkDatabase:RoomDatabase() {

    abstract fun workDao(): WorkDao

    companion object{
        private var instance:WorkDatabase? = null

        fun getInstance(context: Context):WorkDatabase {
            if(instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkDatabase::class.java,
                    "work_database"
                ).build()
            }
            return instance!!
        }
    }
}