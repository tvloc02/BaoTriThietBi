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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    /**
     * Phương thức chính khởi động ứng dụng
     *
     * @param args tham số dòng lệnh
     */
    public static void main(String[] args) {
        SpringApplication.run(UngDungBaoTri.class, args);
        System.out.println("=== HỆ THỐNG QUẢN LÝ BẢO TRÌ THIẾT BỊ ===");
        System.out.println("Ứng dụng đã khởi động thành công!");
        System.out.println("Truy cập: http://localhost:8081");
        System.out.println("========================================");
    }

    /**
     * Bean ModelMapper để chuyển đổi giữa Entity và DTO
     *
     * @return ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Cấu hình mapping strategy
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper;
    }

    /**
     * Bean PasswordEncoder để mã hóa mật khẩu
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}