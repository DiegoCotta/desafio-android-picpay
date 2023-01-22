package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.core.DataError
import com.picpay.desafio.android.core.FlowUseCase
import com.picpay.desafio.android.core.Outcome
import com.picpay.desafio.android.core.Params
import com.picpay.desafio.android.domain.mapper.UserMapper
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class GetUsersUseCase constructor(
    private val postsRepository: UserRepository
) : FlowUseCase<GetUsersUseCase.Request, List<User>> {

    data class Request(val isGetCacheValues: Boolean) : Params

    @Suppress("TooGenericExceptionCaught")
    override fun invoke(params: Request): Flow<Outcome<List<User>>> = flow {
        var isCacheSuccess = false
        if (params.isGetCacheValues) {
            try {
                val cache = postsRepository.getCachedUsers()
                if (cache.isNotEmpty()) {
                    emit(Outcome.Success(UserMapper.transformToList(postsRepository.getCachedUsers())))
                    isCacheSuccess = true
                }
            } catch (ignore: Exception) {
            }
        }

        try {
            val users = postsRepository.getRemoteUsers().filter { it.id != null }
            emit(Outcome.Success(UserMapper.transformToList(users)))
            postsRepository.saveUsers(users)
        } catch (error: HttpException) {
            if (!isCacheSuccess) {
                emit(Outcome.Error(DataError(error.code(), error.message)))
            }
        } catch (e: Exception) {
            if (!isCacheSuccess) {
                emit(Outcome.Error(DataError(0, e.message)))
            }
        }
    }.flowOn(Dispatchers.IO)
}
