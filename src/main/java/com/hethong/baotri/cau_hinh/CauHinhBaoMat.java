package com.hethong.baotri.cau_hinh;

import com.hethong.baotri.dich_vu.nguoi_dung.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class CauHinhBaoMat {  // ✅ Giữ tên class cũ để tránh xung đột

    private final CustomUserDetailsService userDetailsService;

    @Bean
    @Primary  // ✅ Đảm bảo bean này được ưu tiên
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

    // ✅ Simple Success Handler without circular dependency
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            log.info("✅ Đăng nhập thành công cho: {}", authentication.getName());
            response.sendRedirect("/templates/dashboard");
        };
    }

    // ✅ Simple Failure Handler without circular dependency
    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            log.warn("❌ Đăng nhập thất bại: {}", exception.getMessage());
            response.sendRedirect("/login?error=true");
        };
    }

    @Bean
    @Primary  // ✅ Đảm bảo SecurityFilterChain này được ưu tiên
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("🔧 Configuring Spring Security FilterChain...");

        http
                .authorizeHttpRequests(auth -> auth
                        // ✅ Public endpoints
                        .requestMatchers(
                                "/",
                                "/login",
                                "/error",
                                "/api/auth/test",
                                "/api/debug/**",
                                "/debug/**",
                                "/csrf-debug/**",  // ✅ Thêm endpoint debug CSRF
                                "/test-dashboard",
                                "/force-login",
                                "/check-dashboard",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/static/**"
                        ).permitAll()

                        // ✅ All other requests need authentication
                        .anyRequest().authenticated()
                )

                // ✅ Form login configuration
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform-login")  // Form action URL
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(successHandler())     // Custom success handler
                        .failureHandler(failureHandler())     // Custom failure handler
                        .permitAll()
                )

                // ✅ Logout configuration
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )

                // ✅ Exception handling
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("🚫 Unauthorized access to: {}", request.getRequestURI());
                            response.sendRedirect("/login");
                        })
                )

                // ✅ Session management
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/login?expired=true")
                )

                // ✅ CSRF protection - CHỈ disable cho API endpoints
                .csrf(csrf -> csrf
                                .ignoringRequestMatchers(
                                        "/api/auth/**",
                                        "/api/debug/**",
                                        "/debug/**",
                                        "/h2-console/**"  // Nếu dùng H2 database
                                )
                        // ✅ QUAN TRỌNG: Không disable CSRF cho /perform-login
                );

        log.info("✅ Spring Security configuration completed");
        return http.build();
    }
}