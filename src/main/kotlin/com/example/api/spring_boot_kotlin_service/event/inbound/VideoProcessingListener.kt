package com.example.api.spring_boot_kotlin_service.event.inbound

import com.example.api.spring_boot_kotlin_service.event.outbound.dto.VideoUploadedEvent
import kotlinx.coroutines.runBlocking
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class VideoProcessingListener(
    private val videoProcessor: VideoProcessor
) {
    @KafkaListener(
        topics = ["video-uploaded"],
        groupId = "video-group"
    )
    fun consume(event: VideoUploadedEvent) = runBlocking {
        videoProcessor.process(event)
    }
}