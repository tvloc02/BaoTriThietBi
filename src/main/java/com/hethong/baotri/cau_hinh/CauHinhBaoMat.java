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

    // SUCCESS HANDLER - REDIRECT TO /dashboard
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            log.info("✅ LOGIN SUCCESS for: {}", authentication.getName());
            log.info("👤 Authorities: {}", authentication.getAuthorities());

            // REDIRECT TO /dashboard (NOT /templates/dashboard)
            response.sendRedirect("/dashboard");
        };
    }

    // FAILURE HANDLER - REDIRECT TO /login?error=true
    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            log.warn("❌ LOGIN FAILED: {}", exception.getMessage());

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            log.warn("📝 Failed login - Username: {}, Password length: {}",
                    username, password != null ? password.length() : 0);

            response.sendRedirect("/login?error=true");
        };
    }

    @Bean
    @Primary
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("🔧 Configuring Spring Security FilterChain...");

        http
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC ENDPOINTS
                        .requestMatchers(
                                "/",
                                "/login",
                                "/error",
                                "/api/auth/test",
                                "/api/debug/**",
                                "/debug/**",
                                "/csrf-debug/**",
                                "/test-dashboard",
                                "/force-login",
                                "/check-dashboard",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/static/**"
                        ).permitAll()

                        // ALL OTHER REQUESTS NEED AUTHENTICATION
                        .anyRequest().authenticated()
                )

                // FORM LOGIN CONFIGURATION
                .formLogin(form -> form
                        .loginPage("/login")                    // Login page URL
                        .loginProcessingUrl("/perform-login")   // Form action URL
                        .usernameParameter("username")          // Username field name
                        .passwordParameter("password")          // Password field name
                        .successHandler(successHandler())       // Custom success handler
                        .failureHandler(failureHandler())       // Custom failure handler
                        .permitAll()
                )

                // LOGOUT CONFIGURATION
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )

                // EXCEPTION HANDLING
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("🚫 Unauthorized access to: {}", request.getRequestURI());
                            response.sendRedirect("/login");
                        })
                )

                // SESSION MANAGEMENT
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/login?expired=true")
                )

                // CSRF PROTECTION - DISABLE ONLY FOR API ENDPOINTS
                .csrf(csrf -> csrf
                                .ignoringRequestMatchers(
                                        "/api/auth/**",
                                        "/api/debug/**",
                                        "/debug/**",
                                        "/h2-console/**"
                                )
                        // CSRF ENABLED FOR /perform-login (FORM SUBMIT)
                );

        log.info("✅ Spring Security configuration completed");
        return http.build();
    }
}