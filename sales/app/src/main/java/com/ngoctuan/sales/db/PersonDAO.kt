package com.ngoctuan.sales.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
public interface PersonDAO {
    @Query("SELECT * FROM Person ")
    fun getAllPeople(): List<Person>

    @Update
    fun updatePersonByID(person: Person)

    @Insert
    fun insertPerson(person: Person)
    @Query("DELETE FROM Person")
    fun deleteALlPeople()
}