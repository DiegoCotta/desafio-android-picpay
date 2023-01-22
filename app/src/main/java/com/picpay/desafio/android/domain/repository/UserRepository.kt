package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.data.entity.UserEntity

interface UserRepository {
    suspend fun getRemoteUsers(isGetCacheValues: Boolean): List<UserEntity>
    suspend fun getCachedUsers(): List<UserEntity>
    suspend fun saveUsers(users: List<UserEntity>)
}
