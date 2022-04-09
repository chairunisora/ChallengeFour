package com.chairunissa.challengefour.database.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chairunissa.challengefour.database.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao

    companion object {
        @Volatile private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    UserDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun destroyInstance(){
            INSTANCE = null
        }

        fun closeDb() {
            INSTANCE?.close()
        }
    }
}