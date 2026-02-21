package com.example.api.spring_boot_kotlin_service.dto

import java.time.Instant

data class VideoResponse(

    val videoId: String,

    val previewImageUrl: String,

    val gifUrl: String,

    val durationInSeconds: Long,

    val processedAt: Instant
)
