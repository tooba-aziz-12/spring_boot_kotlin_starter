package com.example.api.spring_boot_kotlin_service.controller

import com.example.api.spring_boot_kotlin_service.service.SampleService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder


@ExtendWith(
    *arrayOf(MockitoExtension::class,
    RestDocumentationExtension::class
    )
)
class SampleControllerTest{


    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var sampleService: SampleService

    companion object {
        const val BASE_URI = "/v1/sample"
    }

    @BeforeEach
    fun setUp(
        restDocumentation: RestDocumentationContextProvider
    ) {
        mockMvc = MockMvcBuilders.standaloneSetup(
            SampleController(
                sampleService
            )
        )
            .alwaysDo<StandaloneMockMvcBuilder>(document("{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
            ))
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(restDocumentation)).build()
    }


    @Nested
    inner class GreetUser {


        @Test
        fun withGoodPayload() {
            whenever(sampleService.greetUser("test-user")).thenReturn("greeting string")

            val andReturn: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("$BASE_URI/greeting")
                    .param("name", "test-user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

            Mockito.verify(sampleService, times(1)).greetUser("test-user")
            Assertions.assertEquals(andReturn.response.status, HttpStatus.OK.value())
        }
        @Test
        fun forBadRequest() {

            val andReturn: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("$BASE_URI/greeting")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()

            Mockito.verify(sampleService, times(0)).greetUser("test-user")
            Assertions.assertEquals(andReturn.response.status, HttpStatus.BAD_REQUEST.value())
        }
    }
}