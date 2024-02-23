package id.ac.arraniry.payment.controller

import id.ac.arraniry.payment.dto.AuthenticationRequest
import id.ac.arraniry.payment.dto.AuthenticationResponse
import id.ac.arraniry.payment.dto.RefreshTokenRequest
import id.ac.arraniry.payment.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationService: AuthService
) {

    @PostMapping
    fun authenticate(
        @RequestBody @Valid authenticationRequest: AuthenticationRequest
    ): AuthenticationResponse =
        authenticationService.authentication(authenticationRequest)

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody request: RefreshTokenRequest
    ): AuthenticationResponse =
        authenticationService.refreshAccessToken(request.token)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid refresh token.")


}