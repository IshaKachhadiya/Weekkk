package com.ekta.kotlin.roomdb.todolist.Repository

import androidx.lifecycle.LiveData
import com.ekta.kotlin.roomdb.todolist.DBHelper.PersonDao
import com.ekta.kotlin.roomdb.todolist.DBHelper.PersonInfo
import java.util.Calendar

class PersonRepository (protected val personDao: PersonDao) {


    var allperosn: LiveData<List<PersonInfo>> = personDao.getAllData()


    suspend fun insertPerson(personInfo: PersonInfo){
        personDao.insertData(personInfo)
    }

    suspend fun deletePerson(personInfo: PersonInfo){
        personDao.deleteData(personInfo)
    }

    suspend fun updatePerson(personInfo: PersonInfo){
        personDao.updateInfo(personInfo.id,personInfo.name,personInfo.email_id,personInfo.address,personInfo.ph_no,personInfo.profile_img)
    }
    fun getPersonsByDay(day: String): LiveData<List<PersonInfo>> {
        return personDao.getDataByDay(day)
    }

    // Helper function to get the current day of the week
    private fun getCurrentDayOfWeek(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> ""
        }
    }

}