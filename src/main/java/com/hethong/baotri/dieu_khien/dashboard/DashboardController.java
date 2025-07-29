package com.hethong.baotri.dieu_khien.dashboard;

import com.hethong.baotri.dich_vu.nguoi_dung.NguoiDungService;
import com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final NguoiDungService nguoiDungService;

    /**
     * ✅ DASHBOARD CHÍNH với ERROR HANDLING
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String dashboard(Authentication authentication, Model model) {
        try {
            // ✅ KIỂM TRA AUTHENTICATION
            if (authentication == null || !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getName())) {
                log.warn("⚠️ Unauthorized access to dashboard");
                return "redirect:/login";
            }

            String username = authentication.getName();
            log.info("🏠 Dashboard access by user: {}", username);

            // Lấy thông tin user từ database
            NguoiDung currentUser = nguoiDungService.timNguoiDungTheoTenDangNhap(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            // Lấy danh sách quyền của user
            List<String> userAuthorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Lấy vai trò chính của user (vai trò đầu tiên)
            String primaryRole = currentUser.getVaiTroSet().isEmpty()
                    ? "UNKNOWN"
                    : currentUser.getVaiTroSet().iterator().next().getTenVaiTro();

            log.info("👤 User role: {}, Authorities: {}", primaryRole, userAuthorities);

            // Thiết lập thông tin cơ bản
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("primaryRole", primaryRole);
            model.addAttribute("userAuthorities", userAuthorities);
            model.addAttribute("fullName", currentUser.getHoVaTen());
            model.addAttribute("email", currentUser.getEmail());

            // ✅ PHÂN QUYỀN DASHBOARD THEO VAI TRÒ với TRY-CATCH
            try {
                return switch (primaryRole) {
                    case "QUAN_TRI_VIEN" -> dashboardAdmin(model, currentUser);
                    case "HIEU_TRUONG" -> dashboardHieuTruong(model, currentUser);
                    case "TRUONG_PHONG_CSVC" -> dashboardTruongPhongCSVC(model, currentUser);
                    case "NHAN_VIEN_CSVC" -> dashboardNhanVienCSVC(model, currentUser);
                    case "KY_THUAT_VIEN" -> dashboardKyThuatVien(model, currentUser);
                    case "GIAO_VIEN" -> dashboardGiaoVien(model, currentUser);
                    default -> dashboardDefault(model, currentUser);
                };
            } catch (Exception templateError) {
                log.error("❌ Template error, falling back to default: {}", templateError.getMessage());
                return dashboardDefault(model, currentUser);
            }

        } catch (Exception e) {
            log.error("❌ Error loading dashboard: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Không thể tải dashboard: " + e.getMessage());
            model.addAttribute("title", "Lỗi hệ thống");

            // ✅ FALLBACK TO SIMPLE SUCCESS PAGE
            return dashboardSimple(model, authentication);
        }
    }

    /**
     * 🔧 DASHBOARD ADMIN
     */
    private String dashboardAdmin(Model model, NguoiDung user) {
        log.info("🔧 Loading Admin Dashboard for: {}", user.getTenDangNhap());

        try {
            // Thống kê tổng quan
            model.addAttribute("tongNguoiDung", nguoiDungService.demTongNguoiDung());
            model.addAttribute("tongThietBi", 0); // TODO: implement
            model.addAttribute("tongYeuCauBaoTri", 0); // TODO: implement
            model.addAttribute("tongDoiBaoTri", 0); // TODO: implement

            model.addAttribute("dashboardType", "admin");
            model.addAttribute("title", "Dashboard Quản trị viên");

            return "dashboard/admin";

        } catch (Exception e) {
            log.error("❌ Error loading admin dashboard: {}", e.getMessage());
            throw e; // Rethrow để fallback
        }
    }

    /**
     * 🏫 DASHBOARD HIỆU TRƯỞNG
     */
    private String dashboardHieuTruong(Model model, NguoiDung user) {
        log.info("🏫 Loading Hiệu trưởng Dashboard for: {}", user.getTenDangNhap());
        model.addAttribute("dashboardType", "hieu_truong");
        model.addAttribute("title", "Dashboard Hiệu trưởng");
        return "dashboard/default"; // Fallback to default template
    }

    /**
     * 🏢 DASHBOARD TRƯỞNG PHÒNG CSVC
     */
    private String dashboardTruongPhongCSVC(Model model, NguoiDung user) {
        log.info("🏢 Loading Trưởng phòng CSVC Dashboard for: {}", user.getTenDangNhap());
        model.addAttribute("dashboardType", "truong_phong_csvc");
        model.addAttribute("title", "Dashboard Trưởng phòng CSVC");
        return "dashboard/default"; // Fallback to default template
    }

    /**
     * 👷 DASHBOARD NHÂN VIÊN CSVC
     */
    private String dashboardNhanVienCSVC(Model model, NguoiDung user) {
        log.info("👷 Loading Nhân viên CSVC Dashboard for: {}", user.getTenDangNhap());
        model.addAttribute("dashboardType", "nhan_vien_csvc");
        model.addAttribute("title", "Dashboard Nhân viên CSVC");
        return "dashboard/default"; // Fallback to default template
    }

    /**
     * 🔧 DASHBOARD KỸ THUẬT VIÊN
     */
    private String dashboardKyThuatVien(Model model, NguoiDung user) {
        log.info("🔧 Loading Kỹ thuật viên Dashboard for: {}", user.getTenDangNhap());
        model.addAttribute("dashboardType", "ky_thuat_vien");
        model.addAttribute("title", "Dashboard Kỹ thuật viên");
        return "dashboard/default"; // Fallback to default template
    }

    /**
     * 👨‍🏫 DASHBOARD GIÁO VIÊN
     */
    private String dashboardGiaoVien(Model model, NguoiDung user) {
        log.info("👨‍🏫 Loading Giáo viên Dashboard for: {}", user.getTenDangNhap());
        model.addAttribute("dashboardType", "giao_vien");
        model.addAttribute("title", "Dashboard Giáo viên");
        return "dashboard/default"; // Fallback to default template
    }

    /**
     * ❓ DASHBOARD MẶC ĐỊNH
     */
    private String dashboardDefault(Model model, NguoiDung user) {
        log.info("❓ Loading Default Dashboard for: {}", user.getTenDangNhap());
        model.addAttribute("dashboardType", "default");
        model.addAttribute("title", "Dashboard");
        return "dashboard/default";
    }

    /**
     * 🚨 SIMPLE FALLBACK DASHBOARD - INLINE HTML
     */
    private String dashboardSimple(Model model, Authentication authentication) {
        log.warn("🚨 Using simple fallback dashboard");

        model.addAttribute("title", "Dashboard - Đăng nhập thành công");
        model.addAttribute("username", authentication != null ? authentication.getName() : "Unknown");
        model.addAttribute("authorities", authentication != null ?
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()) : List.of());

        // Return inline template name - sẽ tạo template đơn giản
        return "dashboard/simple";
    }
}