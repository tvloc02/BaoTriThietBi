package com.hethong.baotri.cau_hinh;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
@Slf4j
public class FlywayConfig {

    @Bean
    @DependsOn("dataSource")
    public Flyway flyway(DataSource dataSource) {
        log.info("🔄 Configuring Flyway for H2 database...");

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .validateOnMigrate(false)
                .cleanDisabled(false)
                .mixed(true)
                .sqlMigrationSuffixes(".sql")
                // ✅ H2 specific settings
                .table("flyway_schema_history")
                .baselineVersion("0")
                .baselineDescription("Initial version")
                .load();

        log.info("✅ Flyway configured successfully");
        return flyway;
    }

    @Bean
    @DependsOn("flyway")
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
        log.info("🚀 Starting Flyway migration...");
        return new FlywayMigrationInitializer(flyway);
    }
}