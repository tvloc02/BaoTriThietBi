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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            log.info("🏠 User {} đã đăng nhập, chuyển về dashboard", auth.getName());
            return "redirect:/dashboard";
        } else {
            log.info("🏠 Chưa đăng nhập, chuyển về login");
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String dangNhap(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           @RequestParam(value = "expired", required = false) String expired,
                           Model model) {
        log.info("🔐 Hiển thị trang đăng nhập");

        // Kiểm tra nếu đã đăng nhập rồi
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            log.info("🔄 User {} đã đăng nhập, chuyển về dashboard", auth.getName());
            return "redirect:/dashboard";
        }

        if (error != null) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
            log.warn("❌ Đăng nhập thất bại");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Đăng xuất thành công!");
            log.info("✅ Đăng xuất thành công");
        }

        if (expired != null) {
            model.addAttribute("expiredMessage", "Phiên đăng nhập đã hết hạn!");
            log.warn("⏰ Phiên đăng nhập hết hạn");
        }

        return "nguoi-dung/dang-nhap";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("📊 Truy cập dashboard");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("🔍 Authentication: {}", auth);

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            model.addAttribute("tenDangNhap", auth.getName());
            model.addAttribute("authorities", auth.getAuthorities());

            // Thêm dữ liệu dashboard
            model.addAttribute("totalUsers", "1,247");
            model.addAttribute("totalOrders", "10,293");
            model.addAttribute("totalSales", "$89,000");
            model.addAttribute("totalPending", "2,040");
            model.addAttribute("notifications", 3);

            log.info("✅ Dashboard - User: {}, Authorities: {}", auth.getName(), auth.getAuthorities());

            // ✅ QUAN TRỌNG: Trả về template mới với sidebar layout
            return "dashboard-new";
        } else {
            log.warn("⚠️ Chưa đăng nhập, chuyển về login");
            return "redirect:/login";
        }
    }

    // ✅ Test endpoint không cần authentication
    @GetMapping("/test-dashboard")
    public String testDashboard(Model model) {
        log.info("🧪 Test dashboard access (no auth required)");

        model.addAttribute("tenDangNhap", "test-user");
        model.addAttribute("authorities", "ROLE_ADMIN");
        model.addAttribute("totalUsers", "999");
        model.addAttribute("totalOrders", "555");
        model.addAttribute("totalSales", "$123,456");
        model.addAttribute("totalPending", "111");
        model.addAttribute("notifications", 0);

        return "dashboard";
    }

    // ✅ Route cho các trang khác theo layout mới
    @GetMapping("/nguoi-dung")
    public String nguoiDung(Model model) {
        log.info("👥 Truy cập danh sách người dùng");
        return "nguoi-dung/danh-sach-nguoi-dung-new";
    }

    @GetMapping("/nguoi-dung/them")
    public String themNguoiDung(Model model) {
        log.info("➕ Truy cập thêm người dùng");
        return "nguoi-dung/them-nguoi-dung-new";
    }

    @GetMapping("/thiet-bi")
    public String thietBi(Model model) {
        log.info("🔧 Truy cập danh sách thiết bị");
        return "thiet-bi/danh-sach-thiet-bi-new";
    }

    @GetMapping("/thiet-bi/them")
    public String themThietBi(Model model) {
        log.info("➕ Truy cập thêm thiết bị");
        return "thiet-bi/them-thiet-bi-new";
    }

    @GetMapping("/vat-tu")
    public String vatTu(Model model) {
        log.info("📦 Truy cập danh sách vật tư");
        return "vat-tu/danh-sach-vat-tu-new";
    }

    @GetMapping("/bao-tri/yeu-cau")
    public String yeuCauBaoTri(Model model) {
        log.info("🔧 Truy cập yêu cầu bảo trì");
        return "bao-tri/yeu-cau-bao-tri-new";
    }

    @GetMapping("/bao-cao/tong-hop")
    public String baoCaoTongHop(Model model) {
        log.info("📊 Truy cập báo cáo tổng hợp");
        return "bao-cao/bao-cao-tong-hop-new";
    }

    @GetMapping("/error")
    public String errorPage() {
        log.info("❌ Truy cập trang lỗi");
        return "error/404";
    }
}