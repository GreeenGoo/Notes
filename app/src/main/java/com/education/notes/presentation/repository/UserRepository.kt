package com.education.notes.presentation.repository

import androidx.lifecycle.LiveData
import com.education.notes.presentation.data.UserDao
import com.education.notes.presentation.model.User

class UserRepository(private val userDao: UserDao) {
    val readAllData: LiveData<List<User>> = userDao.readAllData()

    fun addUser(user: User) {
        userDao.addUser(user)
    }

    fun updateUser(user: User){
        userDao.updateUser(user)
    }

    fun deleteUser (user: User){
        userDao.deleteUser(user)
    }

    fun deleteAllUsers(){
        userDao.deleteAllUsers()
    }
}