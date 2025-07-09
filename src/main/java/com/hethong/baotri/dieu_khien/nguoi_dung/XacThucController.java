package com.hethong.baotri.dieu_khien.nguoi_dung;

import com.hethong.baotri.dich_vu.nguoi_dung.NguoiDungService;
import com.hethong.baotri.dto.nguoi_dung.DangNhapDTO;
import com.hethong.baotri.dto.nguoi_dung.NguoiDungDTO;
import com.hethong.baotri.tien_ich.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class XacThucController {

    private final AuthenticationManager authenticationManager;
    private final NguoiDungService nguoiDungService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/dang-nhap")
    public ResponseEntity<Map<String, Object>> dangNhap(@Valid @RequestBody DangNhapDTO dangNhapDTO) {
        log.info("Yêu cầu đăng nhập từ: {}", dangNhapDTO.getTenDangNhap());

        try {
            authenticate(dangNhapDTO.getTenDangNhap(), dangNhapDTO.getMatKhau());

            final UserDetails userDetails = nguoiDungService.loadUserByUsername(dangNhapDTO.getTenDangNhap());
            final String token = jwtTokenUtil.generateToken(userDetails);

            // Cập nhật lần đăng nhập cuối
            nguoiDungService.capNhatLanDangNhapCuoi(dangNhapDTO.getTenDangNhap());

            // Lấy thông tin người dùng
            NguoiDungDTO nguoiDungDTO = nguoiDungService.timNguoiDungTheoTenDangNhap(dangNhapDTO.getTenDangNhap())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("nguoiDung", nguoiDungDTO);
            response.put("message", "Đăng nhập thành công");

            log.info("Đăng nhập thành công cho: {}", dangNhapDTO.getTenDangNhap());
            return ResponseEntity.ok(response);

        } catch (DisabledException e) {
            log.warn("Tài khoản bị vô hiệu hóa: {}", dangNhapDTO.getTenDangNhap());
            nguoiDungService.xuLyDangNhapThatBai(dangNhapDTO.getTenDangNhap());
            return ResponseEntity.badRequest().body(Map.of("message", "Tài khoản đã bị vô hiệu hóa"));
        } catch (BadCredentialsException e) {
            log.warn("Thông tin đăng nhập không hợp lệ: {}", dangNhapDTO.getTenDangNhap());
            nguoiDungService.xuLyDangNhapThatBai(dangNhapDTO.getTenDangNhap());
            return ResponseEntity.badRequest().body(Map.of("message", "Tên đăng nhập hoặc mật khẩu không đúng"));
        } catch (Exception e) {
            log.error("Lỗi khi đăng nhập: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "Đăng nhập thất bại"));
        }
    }

    @PostMapping("/dang-ky")
    public ResponseEntity<Map<String, Object>> dangKy(@Valid @RequestBody NguoiDungDTO nguoiDungDTO) {
        log.info("Yêu cầu đăng ký tài khoản: {}", nguoiDungDTO.getTenDangNhap());

        try {
            NguoiDungDTO nguoiDungMoi = nguoiDungService.taoNguoiDung(nguoiDungDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đăng ký tài khoản thành công");
            response.put("nguoiDung", nguoiDungMoi);

            log.info("Đăng ký thành công cho: {}", nguoiDungDTO.getTenDangNhap());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Lỗi khi đăng ký: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/dang-xuat")
    public ResponseEntity<Map<String, String>> dangXuat() {
        // Với JWT, việc đăng xuất chỉ cần client xóa token
        return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công"));
    }

    @PostMapping("/doi-mat-khau")
    public ResponseEntity<Map<String, String>> doiMatKhau(@RequestParam Long idNguoiDung,
                                                          @RequestParam String matKhauCu,
                                                          @RequestParam String matKhauMoi) {
        try {
            nguoiDungService.doiMatKhau(idNguoiDung, matKhauCu, matKhauMoi);
            return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/quen-mat-khau")
    public ResponseEntity<Map<String, String>> quenMatKhau(@RequestParam String email) {
        // TODO: Implement forgot password functionality
        return ResponseEntity.ok(Map.of("message", "Hướng dẫn đặt lại mật khẩu đã được gửi đến email"));
    }

    @GetMapping("/thong-tin-ca-nhan")
    public ResponseEntity<NguoiDungDTO> layThongTinCaNhan(@RequestParam String tenDangNhap) {
        return nguoiDungService.timNguoiDungTheoTenDangNhap(tenDangNhap)
                .map(nguoiDung -> ResponseEntity.ok(nguoiDung))
                .orElse(ResponseEntity.notFound().build());
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}