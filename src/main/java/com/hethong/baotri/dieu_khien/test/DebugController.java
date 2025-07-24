package com.hethong.baotri.dieu_khien.test;

import com.hethong.baotri.kho_du_lieu.nguoi_dung.NguoiDungRepository;
import com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DebugController {

    private final NguoiDungRepository nguoiDungRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/check-database")
    public ResponseEntity<Map<String, Object>> checkDatabase() {
        Map<String, Object> result = new HashMap<>();

        try {
            // Đếm tổng số user
            long totalUsers = nguoiDungRepository.count();
            result.put("totalUsers", totalUsers);

            // Lấy tất cả username
            List<NguoiDung> users = nguoiDungRepository.findAll();
            result.put("usernames", users.stream().map(NguoiDung::getTenDangNhap).toList());

            // Kiểm tra admin có tồn tại không
            boolean adminExists = nguoiDungRepository.existsByTenDangNhap("admin");
            result.put("adminExists", adminExists);

            if (adminExists) {
                var admin = nguoiDungRepository.findByTenDangNhap("admin").orElse(null);
                if (admin != null) {
                    result.put("adminPassword", admin.getMatKhau());
                    result.put("adminActive", admin.getTrangThaiHoatDong());
                    result.put("adminNotLocked", admin.getTaiKhoanKhongBiKhoa());
                }
            }

            result.put("status", "SUCCESS");

        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("error", e.getMessage());
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<Map<String, Object>> createAdmin() {
        Map<String, Object> result = new HashMap<>();

        try {
            // Xóa admin cũ nếu có
            nguoiDungRepository.findByTenDangNhap("admin").ifPresent(
                    admin -> nguoiDungRepository.delete(admin)
            );

            // Tạo admin mới
            NguoiDung newAdmin = new NguoiDung();
            newAdmin.setTenDangNhap("admin");
            newAdmin.setMatKhau(passwordEncoder.encode("123456"));
            newAdmin.setHoVaTen("Admin User");
            newAdmin.setEmail("admin@test.com");
            newAdmin.setTrangThaiHoatDong(true);
            newAdmin.setTaiKhoanKhongBiKhoa(true);

            NguoiDung saved = nguoiDungRepository.save(newAdmin);

            result.put("status", "SUCCESS");
            result.put("message", "Admin created successfully");
            result.put("adminId", saved.getIdNguoiDung());
            result.put("username", saved.getTenDangNhap());
            result.put("encodedPassword", saved.getMatKhau());
            result.put("testPassword", "123456");

        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("error", e.getMessage());
            e.printStackTrace();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/create-admin-get")
    public ResponseEntity<Map<String, Object>> createAdminGet() {
        // Wrapper cho GET request
        return createAdmin();
    }
}