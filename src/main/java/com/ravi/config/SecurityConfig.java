package com.ravi.config;


import com.ravi.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.filters.CorsFilter;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //disable CSRF for Rest APIs
                .csrf(AbstractHttpConfigurer::disable)

                //Stateless Session
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //URL Authorization
                .authorizeHttpRequests((authorize) -> authorize

                    //public APIs
                        .requestMatchers("/api/auth/**").permitAll()

                   //GET APIs accessible by Users and Admins
                        .requestMatchers(HttpMethod.GET, "/api/products/**")
                        .hasAnyRole("CUSTOMER", "ADMIN")

                  //Users Only APIs
                        .requestMatchers("/api/users/**")
                        .hasRole("CUSTOMER")

                  //Admins Only APIs
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                   //Any other request requires authentication
                        .anyRequest().authenticated()
                )
                //Adding filter
                .addFilterBefore(new JwtTokenValidator(),
                        UsernamePasswordAuthenticationFilter.class)

                //Adding CORS Configuration for Cross sit e authentication
                .cors(cors-> cors
                        .configurationSource(corsConfigurationSource()));

        //returning the http build for the configuration
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {

        return new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                // Restrict to trusted domains
                CorsConfiguration corsConfiguration=new  CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Arrays.asList("https://yourapp.com", "https://admin.yourapp.com"));

                // Allow only required methods
                corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));

                // Restrict headers to essentials
                corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

                // Allow credentials (cookies/tokens)
                corsConfiguration.setAllowCredentials(true);

                // Expose headers needed by frontend
                corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));

                // Cache preflight response for 1 hour
                corsConfiguration.setMaxAge(3600L);

//                // Register the configuration for all endpoints
//                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//                source.registerCorsConfiguration("/**", corsConfiguration);

                return corsConfiguration;
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() ;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
