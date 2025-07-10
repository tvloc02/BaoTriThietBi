package com.hethong.baotri.cau_hinh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@Slf4j
public class CauHinhCSDL {

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @EventListener(ApplicationReadyEvent.class)
    public void kiemTraKetNoiCSDL() {
        log.info("Kiểm tra kết nối cơ sở dữ liệu...");
        log.info("Database URL: {}", databaseUrl);
        log.info("Database Username: {}", databaseUsername);
    }

    @Bean
    public void kiemTraCauHinhCSDL(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            log.info("Kết nối cơ sở dữ liệu thành công!");
            log.info("Database Product Name: {}", connection.getMetaData().getDatabaseProductName());
            log.info("Database Product Version: {}", connection.getMetaData().getDatabaseProductVersion());
        } catch (SQLException e) {
            log.error("Lỗi kết nối cơ sở dữ liệu: {}", e.getMessage());
        }
    }
}