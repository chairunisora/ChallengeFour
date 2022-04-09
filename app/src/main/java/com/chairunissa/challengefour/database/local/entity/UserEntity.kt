package com.chairunissa.challengefour.database.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val note: String
)