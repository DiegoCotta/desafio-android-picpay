package com.picpay.desafio.android.fake

import com.picpay.desafio.android.UsersStub
import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.data.source.remote.UserRemoteDataSource

class UserRemoteDataSourceFake(var isError: Boolean = false) : UserRemoteDataSource {
    override suspend fun getUsers(): List<UserEntity> = if (isError) {
        UsersStub.listUsersEntity
    } else {
        throw Exception("Dummy")
    }
}
