package com.example.academy.auth.config;

import com.example.academy.auth.shared.filters.JwtAuthenticationFilter;
import com.example.academy.auth.shared.filters.TraceIdResponseFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TraceIdResponseFilter traceIdResponseFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityProperties securityProperties;


    /**
     * Password encoder bean.
     * <p>
     * This bean provides the password encoder implementation for the security framework.
     * It is used to hash and compare passwords for authentication.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager bean.
     * <p>
     * This bean provides the authentication manager implementation for the security framework.
     * It is used to authenticate users during login.
     *
     * @param config the authentication configuration
     * @return the authentication manager
     * @throws Exception if any error occurs while configuring the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Spring Security filter chain.
     * <p>
     * This filter chain is the root of the security framework's configuration. It is used to
     * configure the security context and its components.
     *
     * @param http the builder for the security context
     * @return the configured security filter chain
     * @throws Exception if any error occurs while configuring the security context
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                // Enable CORS support
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Do not create a session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // Always allow preflight requests
                    auth.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/").permitAll();

                    // Allow access to public paths
                    securityProperties.getPublicPaths()
                            .forEach(path -> auth.requestMatchers(path).permitAll());
                    // Allow access to unauthorized API paths
                    securityProperties.getUnauthorizedApiPaths()
                            .forEach(path -> auth.requestMatchers(path).permitAll());
                    // Allow access to all API paths
                    securityProperties.getAdminApiPaths()
                            .forEach(path -> auth.requestMatchers(path).permitAll());
                    securityProperties.getSuperadminApiPaths()
                            .forEach(path -> auth.requestMatchers(path).permitAll());
                    // Permit all other requests
                    auth.anyRequest().permitAll();
                })
                // Add JWT authentication filter before the default authentication filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Add trace id response filter after the security context filter
                .addFilterAfter(traceIdResponseFilter, SecurityContextHolderFilter.class);
        return http.build();
    }

    /**
     * Provides the CORS configuration source for the security context.
     * <p>
     * This method creates a CORS configuration source that is used to configure the CORS
     * support for the security context.
     *
     * @return the CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Configure the allowed origins
        configuration.setAllowedOrigins(securityProperties.getAllowedOrigins());
        // Configure the allowed headers
        configuration.setAllowedHeaders(securityProperties.getAllowedHeaders());
        // Configure the allowed HTTP methods
        configuration.setAllowedMethods(securityProperties.getAllowedMethods());
        // Configure the exposed headers
        configuration.setExposedHeaders(securityProperties.getExposedHeaders());
        // Configure whether the credentials are allowed
        configuration.setAllowCredentials(securityProperties.isAllowCredentials());
        // Configure the maximum age of the CORS configuration
        configuration.setMaxAge(securityProperties.getMaxAge());

        // Create the CORS configuration source
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Register the CORS configuration for the specified path pattern
        source.registerCorsConfiguration("/**", configuration);
        // Return the CORS configuration source
        return source;
    }
}
