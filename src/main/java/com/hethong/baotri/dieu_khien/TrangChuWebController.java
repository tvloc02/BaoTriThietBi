package com.hethong.baotri.dieu_khien;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class TrangChuWebController {

    @GetMapping("/")
    public String trangChu() {
        log.info("🏠 Truy cập trang chủ - chuyển hướng về dashboard");
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String dangNhap(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        log.info("🔐 Hiển thị trang đăng nhập");

        if (error != null) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
            log.warn("❌ Đăng nhập thất bại");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Đăng xuất thành công!");
            log.info("✅ Đăng xuất thành công");
        }

        return "nguoi-dung/dang-nhap";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("📊 Truy cập dashboard");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("🔍 Authentication object: {}", auth);

        if (auth != null) {
            log.info("👤 Auth name: {}", auth.getName());
            log.info("🔐 Auth authenticated: {}", auth.isAuthenticated());
            log.info("🎭 Auth authorities: {}", auth.getAuthorities());
        }

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            model.addAttribute("tenDangNhap", auth.getName());
            model.addAttribute("authorities", auth.getAuthorities());

            // Thêm dữ liệu cho dashboard
            model.addAttribute("totalUsers", "1,247");
            model.addAttribute("totalOrders", "10,293");
            model.addAttribute("totalSales", "$89,000");
            model.addAttribute("totalPending", "2,040");
            model.addAttribute("notifications", 3);

            log.info("✅ Dashboard - User: {}, Authorities: {}", auth.getName(), auth.getAuthorities());

            // ✅ SỬA: Trả về đúng tên template
            return "dashboard"; // Thay vì "trang-chu/dashboard"
        } else {
            log.warn("⚠️ Chưa đăng nhập hoặc session hết hệu lực, chuyển về login");
            return "redirect:/login";
        }
    }

    // ✅ Route cho các trang khác
    @GetMapping("/nguoi-dung")
    public String nguoiDung(Model model) {
        log.info("👥 Truy cập danh sách người dùng");
        return "nguoi-dung/danh-sach-nguoi-dung";
    }

    @GetMapping("/nguoi-dung/them")
    public String themNguoiDung(Model model) {
        log.info("➕ Truy cập thêm người dùng");
        return "nguoi-dung/them-nguoi-dung";
    }

    @GetMapping("/nguoi-dung/phan-quyen")
    public String phanQuyen(Model model) {
        log.info("🔐 Truy cập phân quyền");
        return "nguoi-dung/phan-quyen";
    }

    @GetMapping("/thiet-bi")
    public String thietBi(Model model) {
        log.info("🔧 Truy cập danh sách thiết bị");
        return "thiet-bi/danh-sach-thiet-bi";
    }

    @GetMapping("/thiet-bi/them")
    public String themThietBi(Model model) {
        log.info("➕ Truy cập thêm thiết bị");
        return "thiet-bi/them-thiet-bi";
    }

    @GetMapping("/vat-tu")
    public String vatTu(Model model) {
        log.info("📦 Truy cập danh sách vật tư");
        return "vat-tu/danh-sach-vat-tu";
    }

    @GetMapping("/bao-tri/yeu-cau")
    public String yeuCauBaoTri(Model model) {
        log.info("🔧 Truy cập yêu cầu bảo trì");
        return "bao-tri/yeu-cau-bao-tri";
    }

    @GetMapping("/bao-cao/tong-hop")
    public String baoCaoTongHop(Model model) {
        log.info("📊 Truy cập báo cáo tổng hợp");
        return "bao-cao/bao-cao-tong-hop";
    }

    // ✅ THÊM: Route debug để test
    @GetMapping("/test-dashboard")
    public String testDashboard(Model model) {
        log.info("🧪 Test dashboard access");

        model.addAttribute("tenDangNhap", "test-user");
        model.addAttribute("authorities", "ROLE_ADMIN");
        model.addAttribute("totalUsers", "999");
        model.addAttribute("totalOrders", "555");
        model.addAttribute("totalSales", "$123,456");
        model.addAttribute("totalPending", "111");

        return "dashboard";
    }

    @GetMapping("/error")
    public String errorPage() {
        log.info("❌ Truy cập trang lỗi");
        return "error/404";
    }
}