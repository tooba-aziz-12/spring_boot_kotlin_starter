package com.example.api.spring_boot_kotlin_service.service

import com.example.api.spring_boot_kotlin_service.fixture.UserFixture.Companion.roles
import com.example.api.spring_boot_kotlin_service.fixture.UserFixture.Companion.user
import com.example.api.spring_boot_kotlin_service.fixture.UserFixture.Companion.userDto
import com.example.api.spring_boot_kotlin_service.model.User
import com.example.api.spring_boot_kotlin_service.repository.RoleRepository
import com.example.api.spring_boot_kotlin_service.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

     private lateinit var userService: UserService

     @Mock
     lateinit var roleRepository: RoleRepository

    @Mock
    lateinit var userRepository: UserRepository

    @Captor
    val userCaptor: ArgumentCaptor<User> =
        ArgumentCaptor.forClass(User::class.java)

    @BeforeEach
    fun setup() {
        this.userService = UserService(
            roleRepository,
            userRepository
        )
    }

    @Nested
    inner class GreetUserTest{

        @Test
        fun greetUser(){


            whenever(roleRepository.findByCodeName(userDto.userRoles.map { it.userRoleName })).thenReturn(roles)

            whenever(userRepository.save(userCaptor.capture())).thenReturn(user)

            val response  = userService.createUser(userDto = userDto)

            Assertions.assertNotNull(response.userId)
            Assertions.assertEquals(response.email, userDto.email)
            Assertions.assertEquals(response.firstName, userDto.firstName)
            Assertions.assertEquals(response.lastName, userDto.lastName)
            Assertions.assertEquals(response.phone, userDto.phone)
            Assertions.assertTrue(response.userRoles.size==1)
            Assertions.assertEquals(response.userRoles[0].userRoleName, userDto.userRoles[0].userRoleName)
            Assertions.assertEquals(response.userRoles[0].userRoleId, roles[0].id)

        }
    }

}