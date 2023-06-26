package de.cofinpro.recipeserver.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * new Spring security 6.0 style provision of SecurityFilterChain bean with the security configuration,
 * as well as PasswordProvider and AuthenticationManager that makes use of our UserDetails persistence.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .oauth2ResourceServer(auth -> auth.jwt(Customizer.withDefaults()))
                .authorizeHttpRequests(auth -> auth
                        // next line needed (esp. DispatcherType.ERROR) to prevent interception of AuthorizationFilter in error dispatch
                        .dispatcherTypeMatchers(DispatcherType.ASYNC, DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/register", "/actuator/shutdown").permitAll()
                        .requestMatchers(HttpMethod.GET, "/error").permitAll()
                        .requestMatchers("/api/recipe/**").authenticated()
                        .anyRequest().denyAll())
                .build();
    }
}
