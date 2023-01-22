package com.picpay.desafio.android.domain.mapper

import com.picpay.desafio.android.UsersStub.listUsers
import com.picpay.desafio.android.UsersStub.listUsersEntity
import com.picpay.desafio.android.UsersStub.user
import com.picpay.desafio.android.UsersStub.userEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMapperTest {

    @Test
    fun `GIVEN UserEntity WHEN call transformTo Should return User`() {
        val response = UserMapper.transformTo(userEntity)
        assertEquals(user, response)
    }

    @Test
    fun `GIVEN list of UserEntity WHEN call transformTo Should return list of  User`() {
        val response = UserMapper.transformToList(listUsersEntity)
        assertEquals(listUsers, response)
    }
}
