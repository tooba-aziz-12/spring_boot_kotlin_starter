package com.example.api.spring_boot_kotlin_service.model

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "sampleEntity")
class SampleEntity : BaseEntity(){

}