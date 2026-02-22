package com.example.api.spring_boot_kotlin_service.service

import com.example.api.spring_boot_kotlin_service.config.S3Properties
import com.example.api.spring_boot_kotlin_service.dto.VideoMetadataRequest
import com.example.api.spring_boot_kotlin_service.dto.VideoResponse
import com.example.api.spring_boot_kotlin_service.event.outbound.VideoEventPublisher
import com.example.api.spring_boot_kotlin_service.event.outbound.dto.VideoUploadedEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class VideoService(
    private val s3Client: S3Client,
    private val s3Properties: S3Properties,
    private val publisher: VideoEventPublisher
) {
    private val log = LoggerFactory.getLogger(VideoService::class.java)


    suspend fun uploadAndPublish(metadata: VideoMetadataRequest, file: MultipartFile): VideoResponse =
        withContext(Dispatchers.IO) {

            val ext = file.originalFilename?.substringAfterLast('.', missingDelimiterValue = "mp4") ?: "mp4"
            val s3Key = "${metadata.videoId}.$ext"

            file.inputStream.use { input ->
                s3Client.putObject(
                    PutObjectRequest.builder()
                        .bucket(s3Properties.bucket)
                        .key(s3Key)
                        .contentType(file.contentType ?: "application/octet-stream")
                        .build(),
                    RequestBody.fromInputStream(input, file.size)
                )
            }

            // 2) Kafka publish is best-effort (don’t fail the API if Kafka is down)
            val event = VideoUploadedEvent(
                videoId = metadata.videoId,
                s3Key = s3Key,
                uploadedBy = metadata.uploadedBy
            )

            try {
                publisher.publishVideoUploaded(event)
                    .whenComplete { _, ex ->
                        if (ex != null) { // kafka broker down, network issue, timeout etc
                            log.error("Failed to publish video-uploaded event for videoId=${event.videoId}", ex)
                            // TODO: enqueue retry / outbox in production
                        } else {
                            log.info("Published video-uploaded event for videoId=${event.videoId}")
                        }
                    }
            } catch (ex: Exception) {
                // e.g. immediate serialization/config error or Null pointer before send, Producer not initialized
                log.error("Kafka publish threw immediately for videoId=${event.videoId}", ex)
                // TODO: enqueue retry / outbox in production
            }

            VideoResponse(
                videoId = metadata.videoId,
                status = "UPLOADED",
                s3Key = s3Key,
                uploadedAt = metadata.uploadedAt
            )
        }
}