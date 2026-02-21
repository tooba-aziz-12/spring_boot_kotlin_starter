package com.example.api.spring_boot_kotlin_service.service

import com.example.api.spring_boot_kotlin_service.exception.UserCreationFailedException
import com.example.api.spring_boot_kotlin_service.exception.UserNotFoundException
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
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    private lateinit var userService: UserService

    @Mock
    lateinit var roleRepository: RoleRepository

    @Mock
    lateinit var userRepository: UserRepository

    @Captor
    lateinit var userCaptor: ArgumentCaptor<User>

    @BeforeEach
    fun setup() {
        userService = UserService(roleRepository, userRepository)
    }

    @Nested
    inner class CreateUserTest {

        @Test
        fun `should create user successfully`() {
            whenever(roleRepository.findByCodeNameIn(userDto.userRoles.map { it.userRoleName.name }))
                .thenReturn(roles)

            whenever(userRepository.save(userCaptor.capture()))
                .thenReturn(user)

            val response = userService.createUser(userDto)

            val savedUser = userCaptor.value

            Assertions.assertNotNull(savedUser)
            Assertions.assertEquals(userDto.email, savedUser.email)
            Assertions.assertEquals(userDto.firstName, savedUser.firstName)
            Assertions.assertEquals(userDto.lastName, savedUser.lastName)
            Assertions.assertEquals(userDto.phone, savedUser.phone)

            Assertions.assertNotNull(response.userId)
            Assertions.assertEquals(userDto.email, response.email)
            Assertions.assertEquals(1, response.userRoles.size)

            Mockito.verify(roleRepository, times(1))
                .findByCodeNameIn(userDto.userRoles.map { it.userRoleName.name })
            Mockito.verify(userRepository, times(1)).save(savedUser)
        }

        @Test
        fun `should throw UserCreationFailedException when repository fails`() {
            whenever(roleRepository.findByCodeNameIn(userDto.userRoles.map { it.userRoleName.name }))
                .thenReturn(roles)

            whenever(userRepository.save(any<User>()))
                .thenThrow(RuntimeException())

            Assertions.assertThrows(UserCreationFailedException::class.java) {
                userService.createUser(userDto)
            }

            Mockito.verify(roleRepository, times(1))
                .findByCodeNameIn(userDto.userRoles.map { it.userRoleName.name })
            Mockito.verify(userRepository, times(1)).save(any())
        }
    }

    @Nested
    inner class GetUserByIdTest {

        @Test
        fun `should return user when found`() {
            val userId = "test-user-id"
            whenever(userRepository.findById(userId))
                .thenReturn(Optional.of(user))

            val response = userService.getUserById(userId)

            Assertions.assertEquals(user.email, response.email)
            Assertions.assertEquals(user.firstName, response.firstName)
            Assertions.assertEquals(user.lastName, response.lastName)
            Assertions.assertEquals(user.phone, response.phone)
            Assertions.assertEquals(1, response.userRoles.size)

            Mockito.verify(userRepository, times(1)).findById(userId)
        }

        @Test
        fun `should throw UserNotFoundException when user does not exist`() {
            val userId = "wrong-user-id"

            whenever(userRepository.findById(userId))
                .thenReturn(Optional.empty())

            Assertions.assertThrows(UserNotFoundException::class.java) {
                userService.getUserById(userId)
            }

            Mockito.verify(userRepository, times(1)).findById(userId)
        }
    }
}