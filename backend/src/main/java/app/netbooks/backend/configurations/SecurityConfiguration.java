package app.netbooks.backend.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import app.netbooks.backend.authentication.RestAuthenticationEntryPoint;

import org.springframework.security.config.Customizer;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(
                exception -> exception.authenticationEntryPoint(
                    authenticationEntryPoint
                )
            ).authorizeHttpRequests(
                auth -> auth.anyRequest().permitAll()
            ).sessionManagement(sess -> 
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ).build();
    };
};