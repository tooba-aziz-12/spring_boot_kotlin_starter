package com.example.api.spring_boot_kotlin_service.event.outbound.dto
data class VideoUploadedEvent(
    val videoId: String,
    val s3Key: String,
    val uploadedBy: String
)