package id.ac.arraniry.payment.config

import id.ac.arraniry.payment.repo.ConsumerRepo
import id.ac.arraniry.payment.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val authenticationProvider: AuthenticationProvider
) {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): DefaultSecurityFilterChain {
        http
            .csrf{ it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/auth", "/auth/refresh", "/error")
                    .permitAll()
                    .requestMatchers("/consumers**").hasRole("ADMIN")
                    .requestMatchers("/payments**").hasAnyRole("ADMIN", "BANK")
                    .requestMatchers(HttpMethod.POST, "/customer-categories").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/customer-categories/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/customer-categories/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/customers/*/invoices/*/payments/reversal").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/customers/*/invoices/*/payments").hasAnyRole("ADMIN", "BANK")
                    .requestMatchers(HttpMethod.POST, "/customers").hasAnyRole("ADMIN", "APP")
                    .requestMatchers(HttpMethod.PUT, "/customers/**").hasAnyRole("ADMIN", "APP")
                    .requestMatchers(HttpMethod.DELETE, "/customers/**").hasAnyRole("ADMIN", "APP")
                    .requestMatchers(HttpMethod.POST, "/item-categories").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/item-categories/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/item-categories/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/items").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/items/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/items/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/invoices/**").hasAnyRole("ADMIN", "APP")
                    .requestMatchers(HttpMethod.POST, "/channels").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/channels/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/channels/**").hasRole("ADMIN")
                    .anyRequest()
                    .fullyAuthenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}