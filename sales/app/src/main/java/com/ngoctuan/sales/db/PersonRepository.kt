package com.ngoctuan.sales.db

import android.app.Application

class PersonRepository(app: Application) {
    private val personDAO: PersonDAO

    init {
        val personDB: PersonDB = PersonDB.getInstance(app)
        personDAO = personDB.getAccountDao()
    }

    fun updatePersonByID(acc: Person) = personDAO.updatePersonByID(acc)
    fun insertPerson(acc: Person) = personDAO.insertPerson(acc)
    fun getAllPeople() = personDAO.getAllPeople()
    fun deleteAllPeople() = personDAO.deleteALlPeople()

    fun searchName(name: String) = personDAO.searchName(name)

    fun checkExistPhone(phoneNumber: String) = personDAO.checkExistPhone(phoneNumber)
}