package com.example.api.spring_boot_kotlin_service.service

import com.example.api.spring_boot_kotlin_service.dto.VideoRequest
import com.example.api.spring_boot_kotlin_service.dto.VideoResponse
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class VideoService {

    suspend fun processVideo(request: VideoRequest): VideoResponse =
        withContext(Dispatchers.IO) {

            coroutineScope {

                val previewDeferred = async { generatePreview(request.videoUrl) }
                val gifDeferred = async { generateGif(request.videoUrl) }
                val metadataDeferred = async { extractDuration(request.videoUrl) }

                VideoResponse(
                    videoId = request.videoId,
                    previewImageUrl = previewDeferred.await(),
                    gifUrl = gifDeferred.await(),
                    durationInSeconds = metadataDeferred.await(),
                    processedAt = Instant.now()
                )
            }
        }


    private suspend fun generatePreview(videoUrl: String): String {
        delay(500) // simulate IO work
        return "$videoUrl/preview.jpg"
    }

    private suspend fun generateGif(videoUrl: String): String {
        delay(700) // simulate IO work
        return "$videoUrl/preview.gif"
    }

    private suspend fun extractDuration(videoUrl: String): Long {
        delay(300) // simulate metadata extraction
        return 120L
    }
}
