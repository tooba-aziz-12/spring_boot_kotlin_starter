package com.example.api.spring_boot_kotlin_service.event.inbound

import com.example.api.spring_boot_kotlin_service.event.outbound.VideoEventPublisher
import com.example.api.spring_boot_kotlin_service.event.outbound.dto.VideoProcessedEvent
import com.example.api.spring_boot_kotlin_service.event.outbound.dto.VideoUploadedEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.core.sync.RequestBody

@Service
class VideoProcessor(
    private val s3Client: S3Client,
    private val s3Properties: com.example.api.spring_boot_kotlin_service.config.S3Properties,
    private val publisher: VideoEventPublisher
) {

    suspend fun process(event: VideoUploadedEvent) = coroutineScope {
        val durationDeferred = async { extractMetadataDurationSeconds(event.s3Key) }
        val gifKeyDeferred = async { generateGifAndUpload(event.videoId, event.s3Key) }
        val transcriptDeferred = async { transcribe(event.s3Key) }

        val processed = VideoProcessedEvent(
            videoId = event.videoId,
            durationSeconds = durationDeferred.await(),
            gifS3Key = gifKeyDeferred.await(),
            transcript = transcriptDeferred.await()
        )

        // TODO: persist processed results (DB) - keep as next step if needed
        publisher.publishVideoProcessed(processed)
    }

    private suspend fun extractMetadataDurationSeconds(videoS3Key: String): Long =
        withContext(Dispatchers.IO) {
            // Replace with real metadata extraction later
            delay(200)
            120L
        }

    private suspend fun generateGifAndUpload(videoId: String, videoS3Key: String): String =
        withContext(Dispatchers.IO) {
            // Replace with real GIF generation later
            delay(400)

            val gifBytes = "fake-gif".toByteArray()
            val gifKey = "$videoId.gif"

            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(s3Properties.bucket)
                    .key(gifKey)
                    .contentType("image/gif")
                    .build(),
                RequestBody.fromBytes(gifBytes)
            )

            gifKey
        }

    private suspend fun transcribe(videoS3Key: String): String =
        withContext(Dispatchers.IO) {
            // Replace with Whisper / real transcription later
            delay(500)
            "sample transcription"
        }
}