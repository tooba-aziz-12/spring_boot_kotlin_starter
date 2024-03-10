package com.example.api.spring_boot_kotlin_service.service

import com.example.api.spring_boot_kotlin_service.dto.UserDto
import com.example.api.spring_boot_kotlin_service.repository.RoleRepository
import org.springframework.stereotype.Service
import toDto

@Service
class UserService (
    val roleRepository: RoleRepository
) {

    fun createUser(userDto: UserDto): UserDto{

        val assignedRoles = userDto.userRoles.map { it.userRoleName }
        val roles = roleRepository.findByCodeName(assignedRoles)
        val user = userDto.toUser(roles)
        return user.toDto()
    }
}