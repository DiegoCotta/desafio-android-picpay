package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.mapper.UserEntityMapper
import com.picpay.desafio.android.data.mapper.UserMapper
import com.picpay.desafio.android.data.source.local.UserDao
import com.picpay.desafio.android.data.source.remote.UserRemoteDataSource
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository

class UserRepositoryImpl constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getRemoteUsers(): List<User> =
        UserMapper.transformToList(remoteDataSource.getUsers())

    override suspend fun getCachedUsers(): List<User> =
        UserMapper.transformToList(userDao.getAll() ?: listOf())

    override suspend fun saveUsers(users: List<User>) {
        userDao.deleteAll()
        userDao.insertAll(UserEntityMapper.transformToList(users))
    }
}
