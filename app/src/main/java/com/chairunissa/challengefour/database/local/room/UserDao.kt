package com.chairunissa.challengefour.database.local.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.chairunissa.challengefour.database.local.entity.UserEntity

interface UserDao {
    @Query("SELECT * FROM user ORDER BY id DESC")
    fun getNotes(): List<UserEntity>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getNote(id: Int): UserEntity

    @Insert
    fun insertNote(userEntity: UserEntity)

    @Update
    fun updateNote(userEntity: UserEntity)

    @Delete
    fun deleteNote(userEntity: UserEntity)
}