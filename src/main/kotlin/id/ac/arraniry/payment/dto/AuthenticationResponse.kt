package id.ac.arraniry.payment.dto

data class AuthenticationResponse(
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String,
    val refreshExpiresIn: Long,
)
