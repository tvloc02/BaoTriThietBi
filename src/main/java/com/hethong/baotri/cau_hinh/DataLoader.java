package com.hethong.baotri.cau_hinh;

import com.hethong.baotri.dto.nguoi_dung.NguoiDungDTO;
import com.hethong.baotri.dich_vu.nguoi_dung.NguoiDungService;
import com.hethong.baotri.kho_du_lieu.nguoi_dung.NguoiDungRepository;
import com.hethong.baotri.kho_du_lieu.nguoi_dung.VaiTroRepository;
import com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung;
import com.hethong.baotri.thuc_the.nguoi_dung.VaiTro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final NguoiDungRepository nguoiDungRepository;
    private final VaiTroRepository vaiTroRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            taoVaiTroMacDinh();
            taoNguoiDungMacDinh();
            kiemTraPasswordTatCaUser(); // ✅ THÊM: Kiểm tra và sửa password
            log.info("✅ Khởi tạo dữ liệu mặc định thành công!");
        } catch (Exception e) {
            log.error("❌ Lỗi khi khởi tạo dữ liệu mặc định: {}", e.getMessage(), e);
        }
    }

    private void taoVaiTroMacDinh() {
        // Existing code unchanged
    }

    private void taoNguoiDungMacDinh() {
        String username = "admin";
        String defaultPassword = "123456";

        try {
            // ✅ SỬA: Xóa user cũ nếu có để tránh conflict
            nguoiDungRepository.findByTenDangNhap(username).ifPresent(existing -> {
                log.info("Xóa user admin cũ để tạo mới");
                nguoiDungRepository.delete(existing);
            });

            // ✅ Tạo admin mới với password đúng
            NguoiDung admin = new NguoiDung();
            admin.setTenDangNhap(username);
            admin.setMatKhau(passwordEncoder.encode(defaultPassword)); // ✅ Mã hóa đúng
            admin.setHoVaTen("Admin User");
            admin.setEmail("admin@test.com");
            admin.setTrangThaiHoatDong(true);
            admin.setTaiKhoanKhongBiKhoa(true);
            admin.setTaiKhoanKhongHetHan(true);
            admin.setThongTinDangNhapHopLe(true);

            // ✅ Thêm vai trò ADMIN
            Optional<VaiTro> adminRole = vaiTroRepository.findByTenVaiTro("QUAN_TRI_VIEN");
            if (adminRole.isPresent()) {
                admin.getVaiTroSet().add(adminRole.get());
            }

            NguoiDung saved = nguoiDungRepository.save(admin);
            log.info("✅ Tạo tài khoản admin thành công! ID: {}", saved.getIdNguoiDung());
            log.info("📝 Username: {}, Password: {}", username, defaultPassword);

        } catch (Exception e) {
            log.error("❌ Lỗi tạo admin: {}", e.getMessage(), e);
        }
    }

    // ✅ THÊM: Method mới để kiểm tra và sửa password cho tất cả user
    private void kiemTraPasswordTatCaUser() {
        String defaultPassword = "123456";
        String correctPasswordHash = passwordEncoder.encode(defaultPassword);

        log.info("🔐 Kiểm tra và sửa password cho tất cả user...");

        // Danh sách tất cả username từ migration
        String[] usernames = {
                "admin", "hieupho.nguyen", "phong.tran", "duc.le", "mai.pham",
                "thanh.vo", "hung.dao", "linh.nguyen", "minh.tran", "hoa.le"
        };

        for (String username : usernames) {
            try {
                Optional<NguoiDung> userOpt = nguoiDungRepository.findByTenDangNhap(username);
                if (userOpt.isPresent()) {
                    NguoiDung user = userOpt.get();

                    // Kiểm tra password hiện tại có đúng không
                    if (!passwordEncoder.matches(defaultPassword, user.getMatKhau())) {
                        // Nếu sai thì cập nhật
                        user.setMatKhau(passwordEncoder.encode(defaultPassword));
                        user.setTrangThaiHoatDong(true);
                        user.setTaiKhoanKhongBiKhoa(true);
                        user.setTaiKhoanKhongHetHan(true);
                        user.setThongTinDangNhapHopLe(true);
                        user.setSoLanDangNhapThatBai(0);

                        nguoiDungRepository.save(user);
                        log.info("✅ Đã sửa password cho user: {}", username);
                    } else {
                        log.info("✅ User {} đã có password đúng", username);
                    }
                } else {
                    log.warn("⚠️ Không tìm thấy user: {}", username);
                }
            } catch (Exception e) {
                log.error("❌ Lỗi khi kiểm tra password cho user {}: {}", username, e.getMessage());
            }
        }

        log.info("🔐 Hoàn thành kiểm tra password cho tất cả user");
    }

    // ✅ THÊM: Method để test password
    public void testPassword(String username, String password) {
        try {
            Optional<NguoiDung> userOpt = nguoiDungRepository.findByTenDangNhap(username);
            if (userOpt.isPresent()) {
                NguoiDung user = userOpt.get();
                boolean matches = passwordEncoder.matches(password, user.getMatKhau());
                log.info("🔍 Test password cho {}: {} (Hash: {})",
                        username,
                        matches ? "✅ ĐÚNG" : "❌ SAI",
                        user.getMatKhau()
                );
            } else {
                log.warn("⚠️ Không tìm thấy user: {}", username);
            }
        } catch (Exception e) {
            log.error("❌ Lỗi test password: {}", e.getMessage());
        }
    }
}