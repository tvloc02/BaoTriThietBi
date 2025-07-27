package com.hethong.baotri.cau_hinh;

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
            log.info("✅ Khởi tạo dữ liệu mặc định thành công!");
        } catch (Exception e) {
            log.error("❌ Lỗi khi khởi tạo dữ liệu mặc định: {}", e.getMessage(), e);
        }
    }

    private void taoVaiTroMacDinh() {
        // Vai trò đã được tạo trong migration V1
        log.info("✅ Vai trò đã được tạo từ migration");
    }

    private void taoNguoiDungMacDinh() {
        String defaultPassword = "123456";

        // ✅ Tạo đúng các user đã định nghĩa trong migration V1

        // 1. Admin
        if (!nguoiDungRepository.existsByTenDangNhap("admin")) {
            taoUser("admin", "Nguyễn Văn Admin", "admin@truonghoc.edu.vn", "0901234567", defaultPassword, "QUAN_TRI_VIEN");
        }

        // 2. Hiệu trưởng
        if (!nguoiDungRepository.existsByTenDangNhap("hieupho.nguyen")) {
            taoUser("hieupho.nguyen", "Nguyễn Văn Hiệu", "hieupho@truonghoc.edu.vn", "0901234568", defaultPassword, "HIEU_TRUONG");
        }

        // 3. Trưởng phòng CSVC
        if (!nguoiDungRepository.existsByTenDangNhap("phong.tran")) {
            taoUser("phong.tran", "Trần Thị Phòng", "phongcsvc@truonghoc.edu.vn", "0901234569", defaultPassword, "TRUONG_PHONG_CSVC");
        }

        // 4-5. Nhân viên CSVC
        if (!nguoiDungRepository.existsByTenDangNhap("duc.le")) {
            taoUser("duc.le", "Lê Văn Đức", "duc.csvc@truonghoc.edu.vn", "0901234570", defaultPassword, "NHAN_VIEN_CSVC");
        }

        if (!nguoiDungRepository.existsByTenDangNhap("mai.pham")) {
            taoUser("mai.pham", "Phạm Thị Mai", "mai.csvc@truonghoc.edu.vn", "0901234571", defaultPassword, "NHAN_VIEN_CSVC");
        }

        // 6-7. Kỹ thuật viên
        if (!nguoiDungRepository.existsByTenDangNhap("thanh.vo")) {
            taoUser("thanh.vo", "Võ Minh Thành", "thanh.kt@truonghoc.edu.vn", "0901234572", defaultPassword, "KY_THUAT_VIEN");
        }

        if (!nguoiDungRepository.existsByTenDangNhap("hung.dao")) {
            taoUser("hung.dao", "Đào Công Hùng", "hung.kt@truonghoc.edu.vn", "0901234573", defaultPassword, "KY_THUAT_VIEN");
        }

        // 8-10. Giáo viên
        if (!nguoiDungRepository.existsByTenDangNhap("linh.nguyen")) {
            taoUser("linh.nguyen", "Nguyễn Thị Linh", "linh.gv@truonghoc.edu.vn", "0901234574", defaultPassword, "GIAO_VIEN");
        }

        if (!nguoiDungRepository.existsByTenDangNhap("minh.tran")) {
            taoUser("minh.tran", "Trần Văn Minh", "minh.gv@truonghoc.edu.vn", "0901234575", defaultPassword, "GIAO_VIEN");
        }

        if (!nguoiDungRepository.existsByTenDangNhap("hoa.le")) {
            taoUser("hoa.le", "Lê Thị Hoa", "hoa.gv@truonghoc.edu.vn", "0901234576", defaultPassword, "GIAO_VIEN");
        }

        log.info("📝 Đã tạo 10 user đúng như migration V1, tất cả đều có password: {}", defaultPassword);
    }

    private void taoUser(String username, String hoVaTen, String email, String soDienThoai, String password, String vaiTroName) {
        try {
            // Tạo user mới
            NguoiDung user = new NguoiDung();
            user.setTenDangNhap(username);
            user.setMatKhau(passwordEncoder.encode(password)); // ✅ Mã hóa password đúng cách
            user.setHoVaTen(hoVaTen);
            user.setEmail(email);
            user.setSoDienThoai(soDienThoai);
            user.setTrangThaiHoatDong(true);
            user.setTaiKhoanKhongBiKhoa(true);
            user.setTaiKhoanKhongHetHan(true);
            user.setThongTinDangNhapHopLe(true);
            user.setSoLanDangNhapThatBai(0);

            // Thêm vai trò
            Optional<VaiTro> vaiTro = vaiTroRepository.findByTenVaiTro(vaiTroName);
            if (vaiTro.isPresent()) {
                user.getVaiTroSet().add(vaiTro.get());
            } else {
                log.warn("⚠️ Không tìm thấy vai trò: {}", vaiTroName);
            }

            NguoiDung saved = nguoiDungRepository.save(user);
            log.info("✅ Tạo user thành công! Username: {} | Password: {} | Role: {}",
                    username, password, vaiTroName);

        } catch (Exception e) {
            log.error("❌ Lỗi tạo user {}: {}", username, e.getMessage(), e);
        }
    }
}