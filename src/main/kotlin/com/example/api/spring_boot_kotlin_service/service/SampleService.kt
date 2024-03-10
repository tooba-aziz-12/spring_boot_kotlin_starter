package com.example.api.spring_boot_kotlin_service.service

import org.springframework.stereotype.Service

@Service
class SampleService (
) {

    fun greetUser(userName: String): String{
        return "Hi $userName, how are you?"
    }
}