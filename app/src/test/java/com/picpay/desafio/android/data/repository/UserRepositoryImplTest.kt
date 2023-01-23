package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.UsersStub.listUsers
import com.picpay.desafio.android.UsersStub.listUsersEntity
import com.picpay.desafio.android.data.source.local.UserDao
import com.picpay.desafio.android.data.source.remote.UserRemoteDataSourceImpl
import com.picpay.desafio.android.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    private val remoteDataSource: UserRemoteDataSourceImpl = mockk()
    private val userDao: UserDao = mockk()
    private val repository: UserRepository = UserRepositoryImpl(remoteDataSource, userDao)

    private val exception = RuntimeException("dummy")

    @Test
    fun `GIVEN call getRemoteUsers WHEN remoteDataSource return empty list THAN should return empty list`() =
        runTest {
            coEvery { remoteDataSource.getUsers() } returns listOf()
            val response = repository.getRemoteUsers()
            assertEquals(response, listOf())
        }

    @Test
    fun `GIVEN call getRemoteUsers WHEN remoteDataSource return users list THAN should return users list`() =
        runTest {
            coEvery { remoteDataSource.getUsers() } returns listUsersEntity
            val response = repository.getRemoteUsers()
            assertEquals(response, listUsers)
        }

    @Test
    fun `GIVEN call getRemoteUsers WHEN remoteDataSource throw error THAN throw error`() =
        runTest {
            coEvery { remoteDataSource.getUsers() } throws exception
            assertFailsWith(RuntimeException::class) { repository.getRemoteUsers() }
        }

    @Test
    fun `GIVEN call getCachedUsers WHEN userDao return null THAN return empty list`() =
        runTest {
            coEvery { userDao.getAll() } returns null
            val response = repository.getCachedUsers()
            assertEquals(listOf(), response)
        }

    @Test
    fun `GIVEN call getCachedUsers WHEN userDao return list THAN return list`() =
        runTest {
            coEvery { userDao.getAll() } returns listUsersEntity
            val response = repository.getCachedUsers()
            assertEquals(listUsers, response)
        }

    @Test
    fun `GIVEN call getCachedUsers WHEN userDao throw error THAN throw error`() =
        runTest {
            coEvery { userDao.getAll() } throws exception
            assertFailsWith(RuntimeException::class) { repository.getCachedUsers() }
        }

    @Test
    fun `GIVEN call saveUsers WHEN runs THAN return Unit`() =
        runTest {
            every { userDao.deleteAll() } just runs
            every { userDao.insertAll(any()) } just runs
            repository.saveUsers(listUsers)
            verify { userDao.deleteAll() }
            verify { userDao.insertAll(listUsersEntity) }
        }

    @Test
    fun `GIVEN call saveUsers WHEN throw error THAN throw error`() =
        runTest {
            every { userDao.deleteAll() } throws exception
            every { userDao.insertAll(any()) } just runs
            assertFailsWith(RuntimeException::class) { repository.saveUsers(listUsers) }
        }
}
