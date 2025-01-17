package com.picpay.desafio.android.data.source.remote

import com.picpay.desafio.android.data.entity.UserEntity
import retrofit2.Response

@SuppressWarnings("TooGenericExceptionThrown")
class UserRemoteDataSourceImpl constructor(private val service: PicPayService) :
    UserRemoteDataSource {

    override suspend fun getUsers(): List<UserEntity> {
        return getResponse(request = { service.getUsers() })
    }

    private suspend fun <T : Any> getResponse(
        request: suspend () -> Response<T>
    ): T {
        val response = request()
        val body = response.body()
        if (!response.isSuccessful || body != null) {
            return body!!
        } else {
            throw Exception(response.errorBody()?.string() ?: "")
        }
    }
}
