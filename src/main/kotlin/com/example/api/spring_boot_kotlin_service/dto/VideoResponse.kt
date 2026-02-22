package com.example.api.spring_boot_kotlin_service.dto

import java.time.Instant

data class VideoResponse(
    val videoId: String,
    val status: String,
    val s3Key: String,
    val uploadedAt: Instant
)