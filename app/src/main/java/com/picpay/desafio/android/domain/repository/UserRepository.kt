package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.domain.model.User

interface UserRepository {
    suspend fun getRemoteUsers(): List<User>
    suspend fun getCachedUsers(): List<User>
    suspend fun saveUsers(users: List<User>)
}
