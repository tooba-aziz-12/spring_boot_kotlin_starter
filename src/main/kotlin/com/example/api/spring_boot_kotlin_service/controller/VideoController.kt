package com.example.api.spring_boot_kotlin_service.controller

import com.example.api.spring_boot_kotlin_service.dto.VideoRequest
import com.example.api.spring_boot_kotlin_service.dto.VideoResponse
import com.example.api.spring_boot_kotlin_service.service.VideoService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/video")
class VideoController(
    private val videoService: VideoService
) {

    @PostMapping
    suspend fun uploadVideo(
        @RequestBody @Valid request: VideoRequest
    ): VideoResponse {
        return videoService.processVideo(request)
    }
}
