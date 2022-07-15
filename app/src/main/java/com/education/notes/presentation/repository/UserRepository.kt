package com.education.notes.presentation.repository

import androidx.lifecycle.LiveData
import com.education.notes.presentation.data.UserDao
import com.education.notes.presentation.model.User

class UserRepository(private val userDao: UserDao) {
    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }
}