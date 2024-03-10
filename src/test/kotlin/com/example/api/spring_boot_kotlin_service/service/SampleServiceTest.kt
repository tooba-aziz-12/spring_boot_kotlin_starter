package com.example.api.spring_boot_kotlin_service.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class SampleServiceTest {

     private lateinit var sampleService: SampleService

    @BeforeEach
    fun setup() {
        this.sampleService = SampleService(
        )
    }

    @Nested
    inner class GreetUserTest{

        @Test
        fun greetUser(){
            val greeting  = sampleService.greetUser("test-user")
            Assertions.assertEquals(greeting, "Hi test-user, how are you?")
        }
    }

}