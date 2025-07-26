package com.hethong.baotri.dieu_khien.test;

import com.hethong.baotri.cau_hinh.DataLoader;
import com.hethong.baotri.kho_du_lieu.nguoi_dung.NguoiDungRepository;
import com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class DebugPasswordController {

    private final NguoiDungRepository nguoiDungRepository;
    private final PasswordEncoder passwordEncoder;
    private final DataLoader dataLoader;

    @GetMapping("/test-all-passwords")
    public ResponseEntity<Map<String, Object>> testAllPasswords() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> userResults = new ArrayList<>();

        String testPassword = "123456";

        try {
            List<NguoiDung> allUsers = nguoiDungRepository.findAll();

            for (NguoiDung user : allUsers) {
                Map<String, Object> userResult = new HashMap<>();

                boolean matches = passwordEncoder.matches(testPassword, user.getMatKhau());

                userResult.put("username", user.getTenDangNhap());
                userResult.put("fullName", user.getHoVaTen());
                userResult.put("passwordMatches", matches);
                userResult.put("enabled", user.isEnabled());
                userResult.put("accountNonLocked", user.isAccountNonLocked());
                userResult.put("accountNonExpired", user.isAccountNonExpired());
                userResult.put("credentialsNonExpired", user.isCredentialsNonExpired());
                userResult.put("passwordHash", user.getMatKhau());
                userResult.put("failedAttempts", user.getSoLanDangNhapThatBai());

                // Test with authorities
                try {
                    userResult.put("authorities", user.getAuthorities().toString());
                } catch (Exception e) {
                    userResult.put("authorities", "Error: " + e.getMessage());
                }

                userResults.add(userResult);

                log.info("Test user {}: password={}, enabled={}, locked={}",
                        user.getTenDangNhap(), matches, user.isEnabled(), user.isAccountNonLocked());
            }

            result.put("status", "SUCCESS");
            result.put("testPassword", testPassword);
            result.put("totalUsers", allUsers.size());
            result.put("users", userResults);

            // Tạo summary
            long correctPasswords = userResults.stream()
                    .mapToLong(u -> (Boolean) u.get("passwordMatches") ? 1 : 0)
                    .sum();

            result.put("summary", Map.of(
                    "correctPasswords", correctPasswords,
                    "incorrectPasswords", allUsers.size() - correctPasswords
            ));

        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("error", e.getMessage());
            log.error("Error testing passwords: {}", e.getMessage(), e);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/fix-all-passwords")
    public ResponseEntity<Map<String, Object>> fixAllPasswords() {
        Map<String, Object> result = new HashMap<>();
        List<String> fixedUsers = new ArrayList<>();
        String defaultPassword = "123456";

        try {
            List<NguoiDung> allUsers = nguoiDungRepository.findAll();

            for (NguoiDung user : allUsers) {
                // Kiểm tra password hiện tại
                if (!passwordEncoder.matches(defaultPassword, user.getMatKhau())) {
                    // Fix password và account flags
                    user.setMatKhau(passwordEncoder.encode(defaultPassword));
                    user.setTrangThaiHoatDong(true);
                    user.setTaiKhoanKhongBiKhoa(true);
                    user.setTaiKhoanKhongHetHan(true);
                    user.setThongTinDangNhapHopLe(true);
                    user.setSoLanDangNhapThatBai(0);

                    nguoiDungRepository.save(user);
                    fixedUsers.add(user.getTenDangNhap());

                    log.info("✅ Fixed password for user: {}", user.getTenDangNhap());
                }
            }

            result.put("status", "SUCCESS");
            result.put("fixedUsers", fixedUsers);
            result.put("totalFixed", fixedUsers.size());
            result.put("message", "Fixed passwords for " + fixedUsers.size() + " users");

            log.info("🔐 Fixed passwords for {} users: {}", fixedUsers.size(), fixedUsers);

        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("error", e.getMessage());
            log.error("Error fixing passwords: {}", e.getMessage(), e);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/test-user-password")
    public ResponseEntity<Map<String, Object>> testUserPassword(
            @RequestParam String username,
            @RequestParam String password) {

        Map<String, Object> result = new HashMap<>();

        try {
            Optional<NguoiDung> userOpt = nguoiDungRepository.findByTenDangNhap(username);

            if (userOpt.isPresent()) {
                NguoiDung user = userOpt.get();
                boolean matches = passwordEncoder.matches(password, user.getMatKhau());

                result.put("userExists", true);
                result.put("username", user.getTenDangNhap());
                result.put("fullName", user.getHoVaTen());
                result.put("inputPassword", password);
                result.put("storedPasswordHash", user.getMatKhau());
                result.put("passwordMatches", matches);
                result.put("enabled", user.isEnabled());
                result.put("accountNonLocked", user.isAccountNonLocked());
                result.put("accountNonExpired", user.isAccountNonExpired());
                result.put("credentialsNonExpired", user.isCredentialsNonExpired());
                result.put("failedAttempts", user.getSoLanDangNhapThatBai());

                try {
                    result.put("authorities", user.getAuthorities().toString());
                } catch (Exception e) {
                    result.put("authorities", "Error loading authorities: " + e.getMessage());
                }

                // Test encoding the input password
                String freshHash = passwordEncoder.encode(password);
                result.put("freshHash", freshHash);
                result.put("freshHashMatches", passwordEncoder.matches(password, freshHash));

            } else {
                result.put("userExists", false);
                result.put("message", "User not found: " + username);
            }

            result.put("status", "SUCCESS");

        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("error", e.getMessage());
            log.error("Error testing user password: {}", e.getMessage(), e);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/generate-password-hash")
    public ResponseEntity<Map<String, Object>> generatePasswordHash(@RequestParam String password) {
        Map<String, Object> result = new HashMap<>();

        try {
            String hash = passwordEncoder.encode(password);
            boolean verification = passwordEncoder.matches(password, hash);

            result.put("status", "SUCCESS");
            result.put("password", password);
            result.put("hash", hash);
            result.put("verification", verification);

            log.info("Generated hash for password '{}': {}", password, hash);

        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("error", e.getMessage());
        }

        return ResponseEntity.ok(result);
    }
}