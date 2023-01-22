package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.UsersStub.listUsers
import com.picpay.desafio.android.UsersStub.listUsersEntity
import com.picpay.desafio.android.core.DataError
import com.picpay.desafio.android.core.Outcome
import com.picpay.desafio.android.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetUsersUseCaseTest {

    private val repository: UserRepository = mockk()
    private val getUsersUseCase = GetUsersUseCase(repository)

    private val exception = RuntimeException("dummy")

    @Test
    fun `GIVEN call invoke with cache WHEN has cache and remote is success THAN emit return Success and Success`() =
        runTest {
            coEvery { repository.getCachedUsers() } returns listUsersEntity
            coEvery { repository.getRemoteUsers() } returns listUsersEntity
            coEvery { repository.saveUsers(any()) } just runs
            val response = getUsersUseCase.invoke(GetUsersUseCase.Request(true)).toList()

            assertEquals(
                listOf(
                    Outcome.Success(listUsers),
                    Outcome.Success(listUsers)
                ),
                response
            )
        }

    @Test
    fun `GIVEN call invoke with cache WHEN cache is empty and remote is success THAN emit return  Success`() =
        runTest {
            coEvery { repository.getCachedUsers() } returns listOf()
            coEvery { repository.getRemoteUsers() } returns listUsersEntity
            coEvery { repository.saveUsers(any()) } just runs
            val response = getUsersUseCase.invoke(GetUsersUseCase.Request(true)).toList()

            assertEquals(
                listOf(
                    Outcome.Success(listUsers)
                ),
                response
            )
        }

    @Test
    fun `GIVEN call invoke with cache WHEN  has cache and remote return Error THAN emit return  Success`() =
        runTest {
            coEvery { repository.getCachedUsers() } returns listUsersEntity
            coEvery { repository.getRemoteUsers() } throws exception
            coEvery { repository.saveUsers(any()) } just runs
            val response = getUsersUseCase.invoke(GetUsersUseCase.Request(true)).toList()

            assertEquals(
                listOf(
                    Outcome.Success(listUsers)
                ),
                response
            )
        }

    @Test
    fun `GIVEN call invoke with cache WHEN cache is empty and remote return HttpError THAN emit return error`() =
        runTest {
            coEvery { repository.getCachedUsers() } returns listOf()
            coEvery { repository.getRemoteUsers() } throws
                HttpException(
                    Response.error<ResponseBody>(
                        500,
                        ResponseBody.create(
                            "".toMediaTypeOrNull(),
                            "some content"
                        )
                    )
                )
            coEvery { repository.saveUsers(any()) } just runs
            val response = getUsersUseCase.invoke(GetUsersUseCase.Request(true)).toList()

            assertEquals(
                listOf(
                    Outcome.Error(DataError(500, "HTTP 500 Response.error()"))
                ),
                response
            )
        }

    @Test
    fun `GIVEN call invoke without cache WHEN remote is success THAN should emitSuccess`() =
        runTest {
            coEvery { repository.getCachedUsers() } returns listUsersEntity
            coEvery { repository.getRemoteUsers() } returns listUsersEntity
            coEvery { repository.saveUsers(any()) } just runs
            val response = getUsersUseCase.invoke(GetUsersUseCase.Request(false)).toList()

            assertEquals(
                listOf(
                    Outcome.Success(listUsers)
                ),
                response
            )
        }

    @Test
    fun `GIVEN call invoke without cache WHEN remote return HttpError THAN should Error`() =
        runTest {
            coEvery { repository.getCachedUsers() } returns listUsersEntity
            coEvery { repository.getRemoteUsers() } throws
                HttpException(
                    Response.error<ResponseBody>(
                        500,
                        ResponseBody.create(
                            "".toMediaTypeOrNull(),
                            "some content"
                        )
                    )
                )
            coEvery { repository.saveUsers(any()) } just runs
            val response = getUsersUseCase.invoke(GetUsersUseCase.Request(false)).toList()

            assertEquals(
                listOf(
                    Outcome.Error(DataError(500, "HTTP 500 Response.error()"))
                ),
                response
            )
        }

    @Test
    fun `GIVEN call invoke without cache WHEN remote return Error THAN should Error`() =
        runTest {
            coEvery { repository.getCachedUsers() } returns listUsersEntity
            coEvery { repository.getRemoteUsers() } throws exception
            coEvery { repository.saveUsers(any()) } just runs
            val response = getUsersUseCase.invoke(GetUsersUseCase.Request(false)).toList()

            assertEquals(
                listOf(
                    Outcome.Error(DataError(0, "dummy"))
                ),
                response
            )
        }
}
