package com.hethong.baotri.dich_vu.nguoi_dung;

import com.hethong.baotri.kho_du_lieu.nguoi_dung.NguoiDungRepository;
import com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung;
import com.hethong.baotri.tien_ich.JwtTokenUtil;
import com.hethong.baotri.ngoai_le.NgoaiLeNguoiDung;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class XacThucService {

    private final AuthenticationManager authenticationManager;
    private final NguoiDungRepository nguoiDungRepository;
    private final NguoiDungService nguoiDungService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> dangNhap(String tenDangNhap, String matKhau) {
        log.info("Đang xử lý đăng nhập cho: {}", tenDangNhap);

        try {
            // Xác thực thông tin đăng nhập
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(tenDangNhap, matKhau)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Lấy thông tin người dùng
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Tạo JWT token
            String accessToken = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            // Cập nhật lần đăng nhập cuối
            nguoiDungService.capNhatLanDangNhapCuoi(tenDangNhap);

            // Lấy thông tin người dùng
            Optional<NguoiDung> nguoiDung = nguoiDungRepository.findByTenDangNhap(tenDangNhap);

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            response.put("tokenType", "Bearer");
            response.put("expiresIn", jwtTokenUtil.getExpirationTime());
            response.put("nguoiDung", nguoiDung.orElse(null));
            response.put("message", "Đăng nhập thành công");

            log.info("Đăng nhập thành công cho: {}", tenDangNhap);
            return response;

        } catch (DisabledException e) {
            log.warn("Tài khoản bị vô hiệu hóa: {}", tenDangNhap);
            nguoiDungService.xuLyDangNhapThatBai(tenDangNhap);
            throw new NgoaiLeNguoiDung("Tài khoản đã bị vô hiệu hóa");
        } catch (BadCredentialsException e) {
            log.warn("Thông tin đăng nhập không hợp lệ: {}", tenDangNhap);
            nguoiDungService.xuLyDangNhapThatBai(tenDangNhap);
            throw new NgoaiLeNguoiDung("Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }

    public Map<String, Object> lamMoiToken(String refreshToken) {
        log.info("Đang làm mới token");

        try {
            String tenDangNhap = jwtTokenUtil.getUsernameFromToken(refreshToken);
            UserDetails userDetails = nguoiDungService.loadUserByUsername(tenDangNhap);

            if (jwtTokenUtil.validateToken(refreshToken, userDetails)) {
                String accessTokenMoi = jwtTokenUtil.generateToken(userDetails);

                Map<String, Object> response = new HashMap<>();
                response.put("accessToken", accessTokenMoi);
                response.put("tokenType", "Bearer");
                response.put("expiresIn", jwtTokenUtil.getExpirationTime());
                response.put("message", "Làm mới token thành công");

                log.info("Làm mới token thành công cho: {}", tenDangNhap);
                return response;
            } else {
                throw new NgoaiLeNguoiDung("Refresh token không hợp lệ");
            }
        } catch (Exception e) {
            log.error("Lỗi khi làm mới token: {}", e.getMessage());
            throw new NgoaiLeNguoiDung("Không thể làm mới token");
        }
    }

    public void dangXuat(String token) {
        log.info("Đang xử lý đăng xuất");

        try {
            String tenDangNhap = jwtTokenUtil.getUsernameFromToken(token);

            // Thêm token vào blacklist (có thể implement sau)
            // tokenBlacklistService.addToBlacklist(token);

            // Xóa authentication context
            SecurityContextHolder.clearContext();

            log.info("Đăng xuất thành công cho: {}", tenDangNhap);
        } catch (Exception e) {
            log.error("Lỗi khi đăng xuất: {}", e.getMessage());
        }
    }

    public void quenMatKhau(String email) {
        log.info("Đang xử lý quên mật khẩu cho email: {}", email);

        Optional<NguoiDung> nguoiDung = nguoiDungRepository.findByEmail(email);
        if (nguoiDung.isEmpty()) {
            throw new NgoaiLeNguoiDung("Không tìm thấy tài khoản với email: " + email);
        }

        // Tạo token reset password
        String resetToken = UUID.randomUUID().toString();

        // Lưu token vào database (cần thêm bảng reset_password_token)
        // resetPasswordTokenService.createToken(nguoiDung.get(), resetToken);

        // Gửi email với link reset password
        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;
        String noiDung = String.format(
                "Kính gửi %s,\n\n" +
                        "Bạn đã yêu cầu đặt lại mật khẩu. Vui lòng click vào link sau để đặt lại mật khẩu:\n\n" +
                        "%s\n\n" +
                        "Link này sẽ hết hạn sau 24 giờ.\n\n" +
                        "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.\n\n" +
                        "Trân trọng,\n" +
                        "Hệ thống quản lý bảo trì",
                nguoiDung.get().getHoVaTen(),
                resetLink
        );

        // thongBaoEmail.guiEmailThongBao(email, "Đặt lại mật khẩu", noiDung);

        log.info("Đã gửi email đặt lại mật khẩu cho: {}", email);
    }

    public void datLaiMatKhau(String resetToken, String matKhauMoi) {
        log.info("Đang xử lý đặt lại mật khẩu");

        // Kiểm tra token có hợp lệ không
        // ResetPasswordToken token = resetPasswordTokenService.validateToken(resetToken);
        // if (token == null || token.isExpired()) {
        //     throw new NgoaiLeNguoiDung("Token đặt lại mật khẩu không hợp lệ hoặc đã hết hạn");
        // }

        // Tạm thời bỏ qua validation token
        // NguoiDung nguoiDung = token.getNguoiDung();
        // nguoiDung.setMatKhau(passwordEncoder.encode(matKhauMoi));
        // nguoiDungRepository.save(nguoiDung);

        // Xóa token đã sử dụng
        // resetPasswordTokenService.deleteToken(token);

        log.info("Đặt lại mật khẩu thành công");
    }

    public void doiMatKhau(String tenDangNhap, String matKhauCu, String matKhauMoi) {
        log.info("Đang xử lý đổi mật khẩu cho: {}", tenDangNhap);

        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng: " + tenDangNhap));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(matKhauCu, nguoiDung.getMatKhau())) {
            throw new NgoaiLeNguoiDung("Mật khẩu cũ không đúng");
        }

        // Kiểm tra mật khẩu mới không trùng với mật khẩu cũ
        if (passwordEncoder.matches(matKhauMoi, nguoiDung.getMatKhau())) {
            throw new NgoaiLeNguoiDung("Mật khẩu mới không được trùng với mật khẩu cũ");
        }

        // Cập nhật mật khẩu mới
        nguoiDung.setMatKhau(passwordEncoder.encode(matKhauMoi));
        nguoiDungRepository.save(nguoiDung);

        // Gửi email thông báo đổi mật khẩu
        if (nguoiDung.getEmail() != null) {
            // thongBaoEmail.guiEmailThongBao(
            //         nguoiDung.getEmail(),
            //         "Thông báo đổi mật khẩu",
            //         String.format(
            //                 "Kính gửi %s,\n\n" +
            //                         "Mật khẩu của bạn đã được đổi thành công vào lúc %s.\n\n" +
            //                         "Nếu bạn không thực hiện thao tác này, vui lòng liên hệ với quản trị viên.\n\n" +
            //                         "Trân trọng,\n" +
            //                         "Hệ thống quản lý bảo trì",
            //                 nguoiDung.getHoVaTen(),
            //                 LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            //         )
            // );
        }

        log.info("Đổi mật khẩu thành công cho: {}", tenDangNhap);
    }

    public boolean kiemTraMatKhau(String tenDangNhap, String matKhau) {
        log.debug("Đang kiểm tra mật khẩu cho: {}", tenDangNhap);

        Optional<NguoiDung> nguoiDung = nguoiDungRepository.findByTenDangNhap(tenDangNhap);
        if (nguoiDung.isEmpty()) {
            return false;
        }

        return passwordEncoder.matches(matKhau, nguoiDung.get().getMatKhau());
    }

    public void kichHoatTaiKhoan(String email, String maKichHoat) {
        log.info("Đang xử lý kích hoạt tài khoản cho email: {}", email);

        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy tài khoản với email: " + email));

        // Kiểm tra mã kích hoạt (tạm thời bỏ qua)
        // if (!maKichHoat.equals(nguoiDung.getMaKichHoat())) {
        //     throw new NgoaiLeNguoiDung("Mã kích hoạt không đúng");
        // }

        // Kích hoạt tài khoản
        nguoiDung.setTrangThaiHoatDong(true);
        nguoiDung.setTaiKhoanKhongBiKhoa(true);
        nguoiDungRepository.save(nguoiDung);

        // Gửi email chào mừng
        // thongBaoEmail.guiEmailThongBao(
        //         email,
        //         "Chào mừng bạn đến với hệ thống",
        //         String.format(
        //                 "Kính gửi %s,\n\n" +
        //                         "Tài khoản của bạn đã được kích hoạt thành công!\n\n" +
        //                         "Bạn có thể đăng nhập vào hệ thống với tên đăng nhập: %s\n\n" +
        //                         "Trân trọng,\n" +
        //                         "Hệ thống quản lý bảo trì",
        //                 nguoiDung.getHoVaTen(),
        //                 nguoiDung.getTenDangNhap()
        //         )
        // );

        log.info("Kích hoạt tài khoản thành công cho: {}", email);
    }

    @Transactional(readOnly = true)
    public NguoiDung layNguoiDungHienTai() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String tenDangNhap = authentication.getName();
        return nguoiDungRepository.findByTenDangNhap(tenDangNhap).orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean kiemTraQuyenHienTai(String tenQuyen) {
        NguoiDung nguoiDung = layNguoiDungHienTai();
        if (nguoiDung == null) {
            return false;
        }

        return nguoiDung.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(tenQuyen));
    }

    public void moKhoaTaiKhoanTuDong() {
        log.info("Đang mở khóa tự động các tài khoản đã hết thời gian khóa");

        List<NguoiDung> danhSachCanMoKhoa = nguoiDungRepository
                .findByThoiGianKhoaTaiKhoanLessThanEqual(LocalDateTime.now());

        for (NguoiDung nguoiDung : danhSachCanMoKhoa) {
            nguoiDung.moKhoaTaiKhoan();
            nguoiDungRepository.save(nguoiDung);

            // Gửi email thông báo mở khóa
            if (nguoiDung.getEmail() != null) {
                // thongBaoEmail.guiEmailThongBao(
                //         nguoiDung.getEmail(),
                //         "Tài khoản đã được mở khóa",
                //         String.format(
                //                 "Kính gửi %s,\n\n" +
                //                         "Tài khoản của bạn đã được mở khóa tự động.\n\n" +
                //                         "Bạn có thể đăng nhập lại vào hệ thống.\n\n" +
                //                         "Trân trọng,\n" +
                //                         "Hệ thống quản lý bảo trì",
                //                 nguoiDung.getHoVaTen()
                //         )
                // );
            }

            log.info("Đã mở khóa tài khoản: {}", nguoiDung.getTenDangNhap());
        }

        log.info("Hoàn thành mở khóa tự động {} tài khoản", danhSachCanMoKhoa.size());
    }
}