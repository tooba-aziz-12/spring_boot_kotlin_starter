package com.example.api.spring_boot_kotlin_service.repository

import com.example.api.spring_boot_kotlin_service.constant.UserRole
import com.example.api.spring_boot_kotlin_service.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, String> {

    fun findByCodeName(codeNames: List<UserRole>): List<Role>
}