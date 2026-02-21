package com.example.api.spring_boot_kotlin_service.security.filter

import com.example.api.spring_boot_kotlin_service.constant.RequestHeaders
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.*
import org.mockito.kotlin.*
import org.springframework.security.core.context.SecurityContextHolder

class PreAuthorizationFilterTest {

    private lateinit var filter: PreAuthorizationFilter

    private val request: HttpServletRequest = mock()
    private val response: HttpServletResponse = mock()
    private val filterChain: FilterChain = mock()

    @BeforeEach
    fun setup() {
        SecurityContextHolder.clearContext()
        filter = PreAuthorizationFilter()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should authenticate and populate SecurityContext when headers are valid`() {
        val userId = "b42a435465"
        val permissions = "permission-1,permission-2"

        whenever(request.getHeader(RequestHeaders.IS_VALIDATED)).thenReturn("true")
        whenever(request.getHeader(RequestHeaders.USER_ID)).thenReturn(userId)
        whenever(request.getHeader(RequestHeaders.USER_PERMISSIONS)).thenReturn(permissions)

        filter.doFilterInternal(request, response, filterChain)

        val authentication = SecurityContextHolder.getContext().authentication

        Assertions.assertNotNull(authentication)
        Assertions.assertEquals(userId, authentication.name)

        verify(filterChain, times(1)).doFilter(request, response)
    }

    @Test
    fun `should not authenticate when validation header is invalid`() {
        whenever(request.getHeader(RequestHeaders.IS_VALIDATED)).thenReturn("false")

        filter.doFilterInternal(request, response, filterChain)

        val authentication = SecurityContextHolder.getContext().authentication

        Assertions.assertNull(authentication)
        verify(filterChain, times(1)).doFilter(request, response)
    }

    @Test
    fun `should not authenticate when validation header is missing`() {
        whenever(request.getHeader(RequestHeaders.IS_VALIDATED)).thenReturn(null)

        filter.doFilterInternal(request, response, filterChain)

        val authentication = SecurityContextHolder.getContext().authentication

        Assertions.assertNull(authentication)
        verify(filterChain, times(1)).doFilter(request, response)
    }

    @Test
    fun `should propagate exception and not continue filter chain`() {
        whenever(request.getHeader(RequestHeaders.IS_VALIDATED)).thenThrow(RuntimeException("Boom"))

        Assertions.assertThrows(RuntimeException::class.java) {
            filter.doFilterInternal(request, response, filterChain)
        }

        verify(filterChain, never()).doFilter(request, response)
    }
}