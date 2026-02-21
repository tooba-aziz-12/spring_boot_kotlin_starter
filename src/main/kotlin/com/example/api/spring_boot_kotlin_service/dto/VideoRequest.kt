package com.example.api.spring_boot_kotlin_service.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant

data class VideoRequest(

    @field:NotBlank
    val videoId: String,

    @field:NotBlank
    val uploadedBy: String,

    @field:NotBlank
    val videoUrl: String,

    @field:NotNull
    val uploadedAt: Instant
)
