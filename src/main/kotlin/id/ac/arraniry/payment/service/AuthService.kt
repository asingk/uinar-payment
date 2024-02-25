package id.ac.arraniry.payment.service

import id.ac.arraniry.payment.config.JwtProperties
import id.ac.arraniry.payment.dto.AuthenticationRequest
import id.ac.arraniry.payment.dto.AuthenticationResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties
) {
    fun authentication(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.username,
                authenticationRequest.password
            )
        )

        val user = userDetailsService.loadUserByUsername(authenticationRequest.username)
        val accessToken = createAccessToken(user)
        val refreshToken = createRefreshToken(user)
        return AuthenticationResponse(
            accessToken = accessToken,
            expiresIn = jwtProperties.accessTokenExpiration,
            refreshToken = refreshToken,
            refreshExpiresIn = jwtProperties.refreshTokenExpiration,
        )
    }

    fun refreshAccessToken(refreshToken: String): AuthenticationResponse? {
        val extractedUsername: String?
        try {
            extractedUsername = tokenService.extractUsername(refreshToken)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied")
        }
        return extractedUsername?.let { username ->
            val currentUserDetails = userDetailsService.loadUserByUsername(username)
            if (tokenService.isValid(refreshToken, currentUserDetails, "refresh"))
                return AuthenticationResponse(
                    accessToken = createAccessToken(currentUserDetails),
                    expiresIn = jwtProperties.accessTokenExpiration,
                    refreshToken = refreshToken,
                    refreshExpiresIn = jwtProperties.refreshTokenExpiration,
                )
            else
                null
        }
    }

    private fun createAccessToken(user: UserDetails) = tokenService.generate(
        userDetails = user,
        expirationDate = getAccessTokenExpiration(),
        mapOf("typ" to "Bearer")
    )

    private fun getAccessTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)

    private fun createRefreshToken(user: UserDetails) = tokenService.generate(
        userDetails = user,
        expirationDate = getRefreshTokenExpiration(),
        mapOf("typ" to "refresh")
    )
    private fun getRefreshTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)

}