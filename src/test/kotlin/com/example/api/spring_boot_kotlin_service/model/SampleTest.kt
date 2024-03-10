package com.example.api.spring_boot_kotlin_service.model

import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SampleTest {

    @Test
    fun shouldCreateSampleEntityObject() {
        val sampleEntity = SampleEntity()
        Assertions.assertNotNull(sampleEntity.id)
        Assertions.assertFalse(StringUtils.isBlank(sampleEntity.id))
        Assertions.assertNotNull(sampleEntity.createdAt)
        Assertions.assertNotNull(sampleEntity.updatedAt)
        Assertions.assertEquals("", sampleEntity.createdBy)
        Assertions.assertEquals("", sampleEntity.updatedBy)
    }

    @Test
    fun shouldSetId() {
        val sampleEntity = SampleEntity()
        sampleEntity.id = "sample-id-1"

        Assertions.assertEquals("sample-id-1", sampleEntity.id)
    }
}