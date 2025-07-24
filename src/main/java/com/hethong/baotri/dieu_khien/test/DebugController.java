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
import java.util.Optional;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DebugController {

    private final NguoiDungRepository nguoiDungRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/test-password")
    public ResponseEntity<Map<String, Object>> testPassword(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> result = new HashMap<>();

        try {
            Optional<NguoiDung> userOpt = nguoiDungRepository.findByTenDangNhap(username);

            if (userOpt.isPresent()) {
                NguoiDung user = userOpt.get();
                boolean matches = passwordEncoder.matches(password, user.getMatKhau());

                result.put("userExists", true);
                result.put("username", user.getTenDangNhap());
                result.put("storedPassword", user.getMatKhau());
                result.put("inputPassword", password);
                result.put("passwordMatches", matches);
                result.put("enabled", user.isEnabled());
                result.put("accountNonLocked", user.isAccountNonLocked());
                result.put("accountNonExpired", user.isAccountNonExpired());
                result.put("credentialsNonExpired", user.isCredentialsNonExpired());
                result.put("authorities", user.getAuthorities().toString());
            } else {
                result.put("userExists", false);
                result.put("message", "User not found");
            }

            result.put("status", "SUCCESS");

        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("error", e.getMessage());
        }

        return ResponseEntity.ok(result);
    }

}