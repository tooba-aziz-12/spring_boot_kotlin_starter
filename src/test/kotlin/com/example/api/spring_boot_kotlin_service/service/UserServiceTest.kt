package com.example.api.spring_boot_kotlin_service.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

     private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        this.userService = UserService(
        )
    }

    @Nested
    inner class GreetUserTest{

        @Test
        fun greetUser(){
            val greeting  = userService.createUser("test-user")
            Assertions.assertEquals(greeting, "Hi test-user, how are you?")
        }
    }

}