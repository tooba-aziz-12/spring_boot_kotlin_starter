package com.example.api.spring_boot_kotlin_service.security.filter

import com.example.api.spring_boot_kotlin_service.constant.RequestHeaders
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.lang.Boolean.parseBoolean
import java.util.stream.Collectors
import java.util.stream.Stream


@Component
class PreAuthorizationFilter : OncePerRequestFilter() {
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val isValidatedHeader =
            request.getHeader(RequestHeaders.IS_VALIDATED)

        val isValidated = parseBoolean(isValidatedHeader)

        if (isValidated) {

            val userName = request.getHeader(RequestHeaders.USER_ID)

            val rolesHeader = request.getHeader(RequestHeaders.USER_PERMISSIONS)

            val permissions = rolesHeader
                ?.takeIf { it.isNotBlank() }
                ?.split(",")
                ?.toTypedArray()
                ?: emptyArray()

            if (!userName.isNullOrBlank()) {
                injectSecurityPrincipal(userName, permissions)
            }
        }

        filterChain.doFilter(request, response)
    }
    private fun injectSecurityPrincipal(userName: String, permissions: Array<String>) {
        // add authenticated principal to current thread's security pool
        if (SecurityContextHolder.getContext().authentication == null) {
            val authorities: List<GrantedAuthority> = Stream.of(*permissions).map { role: String? ->
                SimpleGrantedAuthority(
                    role
                )
            }.collect(Collectors.toList())
            val userDetails: UserDetails = User(userName, "", authorities)
            val authentication: Authentication = UsernamePasswordAuthenticationToken(userDetails, null, authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }
    }
}