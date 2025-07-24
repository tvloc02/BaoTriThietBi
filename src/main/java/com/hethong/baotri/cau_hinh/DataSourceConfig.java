package com.hethong.baotri.cau_hinh;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Slf4j
@Profile("dev") // Chỉ áp dụng cho profile dev
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();

        // ✅ H2 Database configuration
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:baotri_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;DATABASE_TO_LOWER=TRUE");
        config.setUsername("sa");
        config.setPassword("");

        // ✅ Connection pool settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(20000);
        config.setMaxLifetime(1200000);
        config.setLeakDetectionThreshold(60000);

        // ✅ Performance settings
        config.setAutoCommit(false);
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(3000);

        // ✅ LOẠI BỎ: MySQL-specific connection init SQL
        // Không set connectionInitSql cho H2

        // ✅ Pool name
        config.setPoolName("HikariPool-BaoTri");

        log.info("✅ Configured H2 DataSource with HikariCP");

        return config;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource(hikariConfig());
        log.info("✅ Created H2 DataSource: {}", dataSource.getJdbcUrl());
        return dataSource;
    }
}