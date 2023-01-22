package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.data.source.local.UserDao
import com.picpay.desafio.android.data.source.remote.UserRemoteDataSource
import com.picpay.desafio.android.domain.repository.UserRepository

class UserRepositoryImpl constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getRemoteUsers(): List<UserEntity> =
        remoteDataSource.getUsers()

    override suspend fun getCachedUsers(): List<UserEntity> =
        userDao.getAll() ?: listOf()

    override suspend fun saveUsers(users: List<UserEntity>) {
        userDao.deleteAll()
        userDao.insertAll(users)
    }
}
