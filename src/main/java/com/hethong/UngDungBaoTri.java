package com.hethong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

/**
 * Lớp chính khởi động ứng dụng hệ thống quản lý bảo trì thiết bị
 *
 * @author Đội phát triển hệ thống bảo trì
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = "com.hethong.baotri")
@EnableJpaRepositories(basePackages = "com.hethong.baotri.kho_du_lieu")
@EntityScan(basePackages = "com.hethong.baotri.thuc_the")
@EnableTransactionManagement
@EnableScheduling
public class UngDungBaoTri {

    public static void main(String[] args) {
        SpringApplication.run(UngDungBaoTri.class, args);
        System.out.println("=== HỆ THỐNG QUẢN LÝ BẢO TRÌ THIẾT BỊ ===");
        System.out.println("Ứng dụng đã khởi động thành công!");
        System.out.println("Đăng nhập: http://localhost:8081/");
        System.out.println("Trang chủ: http://localhost:8081/trang-chu");
        System.out.println("API Test:  http://localhost:8081/api/test/hello");
        System.out.println("API Trang chủ: http://localhost:8081/api/trang-chu/thong-ke-tong-quan");
        System.out.println("Database: SQL Server (lov:1433)");
        System.out.println("Demo Login: admin / 123456");
        System.out.println("========================================");
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }
}