package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.data.source.local.UserDao
import com.picpay.desafio.android.data.source.remote.UserRemoteDataSource
import com.picpay.desafio.android.domain.repository.UserRepository

class UserRepositoryImpl constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getRemoteUsers(isGetCacheValues: Boolean): List<UserEntity> {
        val users = remoteDataSource.getUsers().filter { it.id != null }
        userDao.deleteAll()
        userDao.insertAll(users)
        return users
    }

    override suspend fun getCachedUsers(): List<UserEntity> =
        userDao.getAll() ?: listOf()

    override suspend fun saveUsers(users: List<UserEntity>) {
        userDao.deleteAll()
        userDao.insertAll(users)
    }
}
