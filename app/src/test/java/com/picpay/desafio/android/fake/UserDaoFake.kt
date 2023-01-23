package com.picpay.desafio.android.fake

import com.picpay.desafio.android.UsersStub.listUsersEntity
import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.data.source.local.UserDao

class UserDaoFake(val isError: Boolean) : UserDao {
    override fun getAll(): List<UserEntity> =
        if (isError) {
            listUsersEntity
        } else {
            throw Exception("dummy")
        }

    override fun insertAll(movies: List<UserEntity>) = Unit

    override fun deleteAll() = Unit
}
