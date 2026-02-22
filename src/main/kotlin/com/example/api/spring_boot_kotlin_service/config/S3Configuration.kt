package com.example.api.spring_boot_kotlin_service.config

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.HeadBucketRequest
import software.amazon.awssdk.services.s3.model.NoSuchBucketException
import java.net.URI

@ConfigurationProperties(prefix = "aws.s3")
data class S3Properties(
    val endpoint: String? = null,
    val region: String,
    val accessKey: String,
    val secretKey: String,
    val bucket: String
)

@Configuration
@EnableConfigurationProperties(S3Properties::class)
class S3Configuration {

    @Bean
    fun s3Client(props: S3Properties): S3Client {
        val builder = S3Client.builder()
            .region(Region.of(props.region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(props.accessKey, props.secretKey)
                )
            )

        props.endpoint?.let {
            builder.endpointOverride(URI.create(it)).forcePathStyle(true)
        }

        return builder.build()
    }

    @Bean
    fun ensureBucketExists(s3: S3Client, props: S3Properties) = ApplicationRunner {
        try {
            s3.headBucket(HeadBucketRequest.builder().bucket(props.bucket).build())
        } catch (ex: NoSuchBucketException) {
            s3.createBucket(CreateBucketRequest.builder().bucket(props.bucket).build())
        }
    }
}