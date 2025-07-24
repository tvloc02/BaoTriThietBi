package com.hethong.baotri.cau_hinh;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/h2-console/**").permitAll() // Cho phép truy cập H2 Console mà không cần đăng nhập
                                .anyRequest().authenticated() // Các yêu cầu khác yêu cầu xác thực
                )
                .formLogin(form -> form
                        // login usernam == adinim
                        .loginPage("/login") // Trang đăng nhập tùy chỉnh
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // Tắt CSRF cho H2 Console
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                );
        return http.build();
    }
}