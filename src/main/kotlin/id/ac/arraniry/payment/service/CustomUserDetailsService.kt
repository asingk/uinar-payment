package id.ac.arraniry.payment.service

import id.ac.arraniry.payment.entity.Consumer
import id.ac.arraniry.payment.repo.ConsumerRepo
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val consumerRepo: ConsumerRepo
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        consumerRepo.findByUsernameAndDisabled(username, false)
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("Not found!")

    private fun Consumer.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.username)
            .password(this.password)
            .roles(this.role.code)
            .build()
}