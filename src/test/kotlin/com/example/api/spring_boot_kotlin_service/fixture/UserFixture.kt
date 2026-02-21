package com.example.api.spring_boot_kotlin_service.fixture

import com.example.api.spring_boot_kotlin_service.constant.UserRole
import com.example.api.spring_boot_kotlin_service.dto.UserDto
import com.example.api.spring_boot_kotlin_service.dto.UserRoleDto
import com.example.api.spring_boot_kotlin_service.model.Permission
import com.example.api.spring_boot_kotlin_service.model.Role
import com.example.api.spring_boot_kotlin_service.model.User

class UserFixture {

    companion object {

        val role = Role(
            codeName = UserRole.ROLE_1,
            description = "test description",
            permissions = listOf(
                Permission(
                    codeName = "test permission 1",
                    description = "test description for permission 1"
                )
            )
        )

        val user = User(
            firstName = "test-first-name",
            lastName = "test-last-name",
            phone = "test-phone",
            email = "test-email"
        )

        val userRole = com.example.api.spring_boot_kotlin_service.model.UserRole(
            user = user,
            role = role,
            userRoleName = role.codeName
        )

        init {
            // 🔥 LINK BOTH SIDES
            user.userRoles.add(userRole)
        }

        val roles = listOf(role)
        val userRoles = listOf(userRole)

        private val userRolesDto = listOf(
            UserRoleDto(userRoleName = UserRole.ROLE_1)
        )

        val userDto = UserDto(
            firstName = "test-first-name",
            lastName = "test-last-name",
            phone = "test-phone",
            email = "test-email",
            userRoles = userRolesDto
        )

        val createUserBadDto = UserDto(
            firstName = "",
            lastName = "test-last-name",
            phone = "test-phone",
            email = "test-email",
            userRoles = userRolesDto
        )
    }
}