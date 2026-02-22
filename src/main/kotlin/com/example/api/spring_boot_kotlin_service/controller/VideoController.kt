package com.example.api.spring_boot_kotlin_service.controller

import com.example.api.spring_boot_kotlin_service.dto.VideoMetadataRequest
import com.example.api.spring_boot_kotlin_service.dto.VideoResponse
import com.example.api.spring_boot_kotlin_service.service.VideoService
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/video")
class VideoController(
    private val videoService: VideoService
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun uploadVideo(
        @RequestPart("metadata") @Valid metadata: VideoMetadataRequest,
        @RequestPart("file") file: MultipartFile
    ): VideoResponse {
        return videoService.uploadAndPublish(metadata, file)
    }
}