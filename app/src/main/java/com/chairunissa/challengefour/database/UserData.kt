package com.chairunissa.challengefour.database

import com.chairunissa.challengefour.database.local.UserLocalData
import com.chairunissa.challengefour.datauser.UserEntry
import com.chairunissa.challengefour.utils.AppExecutor
import com.chairunissa.challengefour.utils.DataMapper

interface IUserData {
    fun getNotes(callback: (List<UserEntry>) -> Unit)

    fun getNote(id: Int, callback: (UserEntry) -> Unit)

    fun addNote(note: UserEntry)

    fun deleteNote(note: UserEntry)
}

class UserData constructor(
    private val notesLocalDataSource: UserLocalData,
    private val appExecutor: AppExecutor,
) : IUserData {
    companion object {
        @Volatile
        private var instance: UserData? = null

        fun getInstance(
            notesLocalData: UserLocalData,
            appExecutor: AppExecutor
        ): UserData =
            instance ?: synchronized(this) {
                instance ?: UserData(notesLocalData, appExecutor)
            }
    }

    override fun getNotes(
        callback: (List<UserEntry>) -> Unit
    ) {
        appExecutor.diskIO().execute {
            val notes = notesLocalDataSource.getNotes()
            appExecutor.mainThread().execute {
                callback(DataMapper.mapEntitiesToDomain(notes))
            }
        }
    }

    override fun getNote(
        id: Int,
        callback: (UserEntry) -> Unit
    ) {
        appExecutor.diskIO().execute {
            val note = notesLocalDataSource.getNote(id)
            appExecutor.mainThread().execute {
                callback(DataMapper.mapEntityToDomain(note))
            }
        }
    }

    override fun addNote(note: UserEntry) {
        val noteEntity = DataMapper.mapDomainToEntity(note)
        if (note.id > 0) {
            appExecutor.diskIO().execute { notesLocalDataSource.updateNote(noteEntity) }
        } else {
            appExecutor.diskIO().execute { notesLocalDataSource.insertNote(noteEntity) }
        }
    }

    override fun deleteNote(
        note: UserEntry
    ) {
        val noteEntity = DataMapper.mapDomainToEntity(note)
        appExecutor.diskIO().execute {
            notesLocalDataSource.deleteNote(noteEntity)
        }
    }
}