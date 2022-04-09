package com.chairunissa.challengefour.database.local

import com.chairunissa.challengefour.database.local.entity.UserEntity
import com.chairunissa.challengefour.database.local.room.UserDao

class UserLocalData constructor(private val userDao: UserDao){
    companion object{
        private var instance: UserLocalData? = null

        fun getInstance(userDao: UserDao): UserLocalData =
            instance ?: synchronized(this){
                instance ?: UserLocalData(userDao)
            }
    }

    fun getNotes(): List<UserEntity> = userDao.getNotes()

    fun getNote(id: Int): UserEntity = userDao.getNote(id)

    fun insertNote(userEntity: UserEntity) = userDao.insertNote(userEntity)

    fun updateNote(userEntity: UserEntity) = userDao.updateNote(userEntity)

    fun deleteNote(userEntity: UserEntity) = userDao.deleteNote(userEntity)
}

