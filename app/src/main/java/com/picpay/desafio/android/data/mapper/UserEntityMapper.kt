package com.picpay.desafio.android.data.mapper

import com.picpay.desafio.android.core.BaseMapper
import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.domain.model.User

object UserEntityMapper : BaseMapper<User, UserEntity>() {
    override fun transformTo(source: User): UserEntity =
        UserEntity(
            id = source.id,
            username = source.username ?: "",
            name = source.name ?: "",
            img = source.img ?: ""
        )
}
