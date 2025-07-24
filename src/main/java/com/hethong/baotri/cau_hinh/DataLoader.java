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

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final NguoiDungService nguoiDungService;
    private final NguoiDungRepository nguoiDungRepository;
    private final VaiTroRepository vaiTroRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            taoVaiTroMacDinh();
            kiemTraVaTaoNguoiDungMacDinh();
            log.info("✅ Khởi tạo dữ liệu mặc định thành công!");
        } catch (Exception e) {
            log.error("❌ Lỗi khi khởi tạo dữ liệu mặc định: {}", e.getMessage(), e);
        }
    }

    private void taoVaiTroMacDinh() {
        log.info("📝 Kiểm tra và tạo vai trò ADMIN mặc định...");
        if (!vaiTroRepository.existsByTenVaiTro("ADMIN")) {
            VaiTro adminRole = new VaiTro();
            adminRole.setTenVaiTro("ADMIN");
            adminRole.setMoTa("Quản trị viên hệ thống");
            adminRole.setTrangThaiHoatDong(true);
            vaiTroRepository.save(adminRole);
            log.info("✅ Tạo vai trò ADMIN thành công!");
        } else {
            log.info("ℹ️ Vai trò ADMIN đã tồn tại");
        }
    }

    private void kiemTraVaTaoNguoiDungMacDinh() {
        String username = "admin";
        String defaultPassword = "123456";

        try {
            if (!nguoiDungRepository.existsByTenDangNhap(username)) {
                // Chỉ tạo mới nếu chưa tồn tại
                NguoiDungDTO admin = new NguoiDungDTO();
                admin.setTenDangNhap(username);
                admin.setMatKhau(defaultPassword);
                // ... các field khác

                NguoiDungDTO createdAdmin = nguoiDungService.taoNguoiDung(admin);
                log.info("✅ Tạo tài khoản admin thành công!");
            } else {
                // Nếu đã tồn tại, chỉ kiểm tra không reset
                log.info("ℹ️ Tài khoản admin đã tồn tại");
            }
        } catch (Exception e) {
            log.error("❌ Lỗi: {}", e.getMessage(), e);
        }
    }
}