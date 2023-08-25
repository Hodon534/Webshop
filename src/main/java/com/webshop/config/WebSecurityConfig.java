package com.webshop.config;

import com.webshop.model.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration class for Spring Security settings in the web application.
 * This class defines security configurations, including authentication, authorization,
 * CORS (Cross-Origin Resource Sharing), and other security-related settings.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    /**
     * Constant defining the maximum age for CORS (Cross-Origin Resource Sharing) configuration.
     */
    private static final long MAX_AGE = 3600L;

    /**
     * Configures a bean of type BCryptPasswordEncoder, commonly used for password encoding and decoding in Spring Security.
     *
     * @return BCryptPasswordEncoder bean instance.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the main security filter chain for the application.
     *
     * @param http The HTTP security object to configure.
     * @return The configured security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers(AccessConst.RESOURCES_PUBLIC).
                                    permitAll();
                            auth.requestMatchers(AccessConst.RESOURCES_USER)
                                    .hasAnyAuthority(
                                            UserRole.USER.name(),
                                            UserRole.ADMIN.name());
                            auth.requestMatchers(AccessConst.RESOURCES_ADMIN)
                                    .hasAnyAuthority(
                                            UserRole.ADMIN.name());
                        })
                .formLogin(
                        form ->
                                form
                                        .loginPage("/login")
                                        .loginProcessingUrl("/login")
                                        .defaultSuccessUrl("/")
                                        .permitAll())
                .httpBasic(withDefaults())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                )
                .cors(withDefaults())
                .headers(header -> header
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

    /**
     * Configures CORS (Cross-Origin Resource Sharing) settings for the application.
     *
     * @return CorsConfigurationSource for CORS configuration.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        configuration.setMaxAge(MAX_AGE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
