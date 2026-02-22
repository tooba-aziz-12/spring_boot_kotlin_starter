package com.example.api.spring_boot_kotlin_service.event.outbound.dto

data class VideoProcessedEvent(
    val videoId: String,
    val durationSeconds: Long,
    val gifS3Key: String,
    val transcript: String
)