package com.ngoctuan.sales.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Person::class], version = 1)
abstract class PersonDB : RoomDatabase() {
    abstract fun getAccountDao(): PersonDAO

    companion object {
        @Volatile
        private var instance: PersonDB? = null
        fun getInstance(context: Context): PersonDB {
            if (instance == null) {
                instance = Room.databaseBuilder(context, PersonDB::class.java, "salesDB")
                    .allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}