package com.chairunissa.challengefour.utils

import com.chairunissa.challengefour.database.local.entity.UserEntity
import com.chairunissa.challengefour.datauser.UserEntry

object DataMapper {
    fun mapEntitiesToDomain(input: List<UserEntity>): List<UserEntry> =
        input.map {
            UserEntry(
                it.id,
                it.title,
                it.note
            )
        }

    fun mapEntityToDomain(input: UserEntity) = UserEntry(
        input.id,
        input.title,
        input.note
    )

    fun mapDomainToEntity(input: UserEntry) = UserEntity(
        input.id,
        input.title,
        input.note
    )
}