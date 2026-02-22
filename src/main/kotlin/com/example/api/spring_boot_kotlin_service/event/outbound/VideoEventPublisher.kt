package com.example.api.spring_boot_kotlin_service.event.outbound

import com.example.api.spring_boot_kotlin_service.event.outbound.dto.VideoProcessedEvent
import com.example.api.spring_boot_kotlin_service.event.outbound.dto.VideoUploadedEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class VideoEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    fun publishVideoUploaded(event: VideoUploadedEvent): CompletableFuture<SendResult<String, Any>> {
        return kafkaTemplate.send(TOPIC_VIDEO_UPLOADED, event.videoId, event)
    }

    fun publishVideoProcessed(event: VideoProcessedEvent): CompletableFuture<SendResult<String, Any>> {
        return kafkaTemplate.send(TOPIC_VIDEO_PROCESSED, event.videoId, event)
    }

    companion object {
        const val TOPIC_VIDEO_UPLOADED = "video-uploaded"
        const val TOPIC_VIDEO_PROCESSED = "video-processed"
    }
}