package com.hethong.baotri.cau_hinh;

import com.hethong.baotri.dich_vu.nguoi_dung.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class CauHinhBaoMat {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        log.info("🔐 Creating BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        log.info("🔧 Creating DaoAuthenticationProvider");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("🔧 Creating AuthenticationManager");
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            log.info("✅ LOGIN SUCCESS for: {}", authentication.getName());
            log.info("👤 Authorities: {}", authentication.getAuthorities());

            String username = authentication.getName();
            log.info("🔐 Successful login - User: {} - Redirecting to dashboard", username);

            // ✅ REDIRECT TO DASHBOARD (FIXED URL)
            response.sendRedirect("/dashboard");
        };
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            log.warn("❌ LOGIN FAILED: {}", exception.getMessage());

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            log.warn("📝 Failed login attempt - Username: {}, Password provided: {}",
                    username, password != null && !password.isEmpty() ? "YES" : "NO");

            // ✅ REDIRECT BACK TO LOGIN WITH ERROR
            response.sendRedirect("/login?error=true");
        };
    }

    @Bean
    @Primary
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("🔧 Configuring Spring Security FilterChain...");

        http
                .authorizeHttpRequests(auth -> auth
                        // ✅ PUBLIC ENDPOINTS
                        .requestMatchers(
                                "/login",
                                "/error",
                                "/api/debug/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/static/**",
                                "/webjars/**",
                                "/access-denied",
                                "/hybridaction/**" // Ignore browser extension requests
                        ).permitAll()

                        // ✅ ROOT PATH REDIRECT
                        .requestMatchers("/").permitAll()

                        // ✅ ALL OTHER REQUESTS NEED AUTHENTICATION
                        .anyRequest().authenticated()
                )

                // ✅ FORM LOGIN CONFIGURATION (FIXED)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login") // ✅ FIXED: Use underscore instead of hyphen
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(successHandler())
                        .failureHandler(failureHandler())
                        .permitAll()
                )

                // ✅ LOGOUT CONFIGURATION
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )

                // ✅ EXCEPTION HANDLING
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("🚫 Unauthorized access to: {} from IP: {}",
                                    request.getRequestURI(),
                                    request.getRemoteAddr());
                            response.sendRedirect("/login");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.warn("🚫 Access denied to: {} for user: {}",
                                    request.getRequestURI(),
                                    request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous");
                            response.sendRedirect("/access-denied");
                        })
                )

                // ✅ SESSION MANAGEMENT
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/login?expired=true")
                )

                // ✅ CSRF PROTECTION
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/api/**",
                                "/debug/**"
                        )
                );

        log.info("✅ Spring Security configuration completed");
        return http.build();
    }
}