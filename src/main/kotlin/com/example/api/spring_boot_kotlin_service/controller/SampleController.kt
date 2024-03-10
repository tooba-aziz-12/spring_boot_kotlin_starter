package com.example.api.spring_boot_kotlin_service.controller

import com.example.api.spring_boot_kotlin_service.service.SampleService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/sample")
class SampleController (
    val sampleService: SampleService
){


    @GetMapping("/greeting")
    @ResponseStatus(HttpStatus.OK)
    fun greetingsApi(
        @RequestParam name: String
    ): String {
        return sampleService.greetUser(name)
    }
}
