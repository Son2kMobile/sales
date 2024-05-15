package com.ngoctuan.sales.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName

@Dao
interface PersonDAO {
    @Query("SELECT * FROM Person ")
    fun getAllPeople(): Flow<List<Person>>

    @Update(entity = Person::class)
    fun updatePersonByID(person: Person)

    @Insert
    fun insertPerson(person: Person)

    @Query("select *  from Person where number =:phoneNumber or phoneParent =:phoneNumber")
    fun checkExistNumber(phoneNumber: String): Flow<List<Person>>

    @Query("DELETE  FROM Person")
    fun deleteALlPeople()

    @Query("select *  from Person where sheetName =:sheetName ")
    fun getDataBySheetName(sheetName: String): Flow<List<Person>>

    @Query("SELECT * FROM Person where name like :query")
    fun searchName(query: String): Flow<List<Person>>

    @Query("select *  from Person where number =:phoneNumber or phoneParent =:phoneNumber")
    fun checkExistPhone(phoneNumber: String): Flow<Person>
}