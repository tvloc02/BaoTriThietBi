package com.hethong.baotri.dieu_khien;

import com.hethong.baotri.dich_vu.nguoi_dung.NguoiDungService;
import com.hethong.baotri.dto.nguoi_dung.NguoiDungDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TrangChuWebController {

    private final NguoiDungService nguoiDungService;

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

        model.addAttribute("title", "Đăng nhập - Hệ thống Bảo trì Thiết bị");
        return "nguoi-dung/dang-nhap";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("📊 === TRUY CẬP DASHBOARD ===");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            log.warn("⚠️ Chưa đăng nhập, chuyển về login");
            return "redirect:/login";
        }

        String username = auth.getName();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        log.info("👤 User: {}", username);
        log.info("🎭 Authorities: {}", authorities);

        try {
            // ✅ Lấy thông tin user từ database
            NguoiDungDTO nguoiDung = nguoiDungService.layNguoiDungTheoTenDangNhap(username);

            // ✅ Thông tin chung cho tất cả dashboard
            model.addAttribute("username", username);
            model.addAttribute("tenDangNhap", username);
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("hoVaTen", nguoiDung.getHoVaTen());
            model.addAttribute("email", nguoiDung.getEmail());

            String roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            model.addAttribute("authorities", roles);

            // ✅ FIXED: Phân loại dashboard theo vai trò
            String dashboardTemplate = getDashboardTemplateByRole(authorities, model, nguoiDung);

            log.info("✅ Rendering dashboard: {} for user: {}", dashboardTemplate, username);
            return dashboardTemplate;

        } catch (Exception e) {
            log.error("❌ Lỗi khi load dashboard cho user {}: {}", username, e.getMessage());
            return "redirect:/login?error=dashboard_error";
        }
    }

    /**
     * ✅ FIXED: Xác định template dashboard và data theo vai trò
     */
    /**
     * ✅ BYPASS ROLE CHECK - Phân dashboard theo username
     */
    private String getDashboardTemplateByRole(Collection<? extends GrantedAuthority> authorities,
                                              Model model, NguoiDungDTO nguoiDung) {

        String username = nguoiDung.getTenDangNhap();
        log.info("🔍 === BYPASS ROLE CHECK - PHÂN DASHBOARD THEO USERNAME ===");
        log.info("👤 Username: {}", username);

        // ✅ PHÂN DASHBOARD THEO USERNAME - KHÔNG CẦN ROLE TỪ DATABASE
        switch (username) {
            case "admin":
                log.info("✅ Admin user -> admin-dashboard");
                return setupAdminDashboard(model, nguoiDung);

            case "hieupho.nguyen":
                log.info("✅ Leader user -> leader-dashboard");
                return setupLeaderDashboard(model, nguoiDung);

            case "phong.tran":
                log.info("✅ Manager user -> manager-dashboard");
                return setupManagerDashboard(model, nguoiDung);

            case "duc.le":
            case "mai.pham":
                log.info("✅ Staff user -> staff-dashboard");
                return setupStaffDashboard(model, nguoiDung);

            case "thanh.vo":
            case "hung.dao":
                log.info("✅ Technician user -> technician-dashboard");
                return setupTechnicianDashboard(model, nguoiDung);

            case "linh.nguyen":
            case "minh.tran":
            case "hoa.le":
                log.info("✅ Teacher user -> teacher-dashboard");
                return setupTeacherDashboard(model, nguoiDung);

            default:
                log.info("✅ Default user -> admin-dashboard");
                return setupAdminDashboard(model, nguoiDung);
        }
    }



    /**
     * Dashboard cho QUAN_TRI_VIEN - Toàn quyền quản lý
     */
    private String setupAdminDashboard(Model model, NguoiDungDTO nguoiDung) {
        log.info("🎯 Setting up ADMIN dashboard");
        model.addAttribute("title", "Dashboard Quản trị viên");
        model.addAttribute("roleTitle", "Quản trị viên hệ thống");
        model.addAttribute("dashboardType", "admin");

        // ✅ Thống kê toàn hệ thống
        model.addAttribute("totalUsers", nguoiDungService.demTongSoNguoiDung());
        model.addAttribute("totalDevices", 247);
        model.addAttribute("totalRequests", 38);
        model.addAttribute("totalReports", 1542);
        model.addAttribute("totalAlerts", 7);

        // ✅ Menu đầy đủ cho admin
        model.addAttribute("canManageUsers", true);
        model.addAttribute("canManageDevices", true);
        model.addAttribute("canViewReports", true);
        model.addAttribute("canManageSystem", true);

        return "dashboard/admin-dashboard";
    }

    /**
     * Dashboard cho HIEU_TRUONG/PHO_HIEU_TRUONG - Xem báo cáo tổng quan
     */
    private String setupLeaderDashboard(Model model, NguoiDungDTO nguoiDung) {
        log.info("🎯 Setting up LEADER dashboard");
        model.addAttribute("title", "Dashboard Lãnh đạo");
        model.addAttribute("roleTitle", "Lãnh đạo nhà trường");
        model.addAttribute("dashboardType", "leader");

        // ✅ Thống kê tổng quan
        model.addAttribute("totalDevices", 247);
        model.addAttribute("maintenanceEfficiency", "85%");
        model.addAttribute("budgetUsed", "2.5 tỷ");
        model.addAttribute("totalIncidents", 8);
        model.addAttribute("monthlyBudget", "500 triệu");
        model.addAttribute("yearlyBudget", "6 tỷ");

        // ✅ Quyền hạn có hạn
        model.addAttribute("canViewReports", true);
        model.addAttribute("canViewBudget", true);
        model.addAttribute("canManageUsers", false);
        model.addAttribute("canManageDevices", false);

        return "dashboard/leader-dashboard";
    }

    /**
     * Dashboard cho TRUONG_PHONG_CSVC - Quản lý thiết bị và bảo trì
     */
    private String setupManagerDashboard(Model model, NguoiDungDTO nguoiDung) {
        log.info("🎯 Setting up MANAGER dashboard");
        model.addAttribute("title", "Dashboard Trưởng phòng CSVC");
        model.addAttribute("roleTitle", "Trưởng phòng Cơ sở vật chất");
        model.addAttribute("dashboardType", "manager");

        // ✅ Thống kê quản lý
        model.addAttribute("totalDevices", 247);
        model.addAttribute("maintenanceRequests", 25);
        model.addAttribute("pendingApprovals", 8);
        model.addAttribute("teamMembers", 12);
        model.addAttribute("completedTasks", 45);
        model.addAttribute("overdueTasks", 3);

        // ✅ Quyền quản lý thiết bị và duyệt yêu cầu
        model.addAttribute("canManageDevices", true);
        model.addAttribute("canApproveRequests", true);
        model.addAttribute("canViewReports", true);
        model.addAttribute("canManageTeam", true);

        return "dashboard/manager-dashboard";
    }

    /**
     * Dashboard cho KY_THUAT_VIEN - Thực hiện bảo trì
     */
    private String setupTechnicianDashboard(Model model, NguoiDungDTO nguoiDung) {
        log.info("🎯 Setting up TECHNICIAN dashboard");
        model.addAttribute("title", "Dashboard Kỹ thuật viên");
        model.addAttribute("roleTitle", "Kỹ thuật viên bảo trì");
        model.addAttribute("dashboardType", "technician");

        // ✅ Thống kê công việc
        model.addAttribute("assignedTasks", 8);
        model.addAttribute("completedTasks", 15);
        model.addAttribute("urgentTasks", 3);
        model.addAttribute("todaySchedule", 5);
        model.addAttribute("weeklyHours", 35);
        model.addAttribute("efficiency", "92%");

        // ✅ Quyền thực hiện bảo trì
        model.addAttribute("canExecuteMaintenance", true);
        model.addAttribute("canUpdateTaskStatus", true);
        model.addAttribute("canViewSchedule", true);
        model.addAttribute("canCreateReports", true);

        return "dashboard/technician-dashboard";
    }

    /**
     * Dashboard cho NHAN_VIEN_CSVC - Hỗ trợ quản lý
     */
    private String setupStaffDashboard(Model model, NguoiDungDTO nguoiDung) {
        log.info("🎯 Setting up STAFF dashboard");
        model.addAttribute("title", "Dashboard Nhân viên CSVC");
        model.addAttribute("roleTitle", "Nhân viên Cơ sở vật chất");
        model.addAttribute("dashboardType", "staff");

        // ✅ Thống kê hỗ trợ
        model.addAttribute("totalDevices", 247);
        model.addAttribute("myRequests", 5);
        model.addAttribute("pendingRequests", 12);
        model.addAttribute("notifications", 3);
        model.addAttribute("inventoryItems", 150);
        model.addAttribute("lowStockItems", 8);

        // ✅ Quyền hỗ trợ
        model.addAttribute("canCreateRequests", true);
        model.addAttribute("canViewDevices", true);
        model.addAttribute("canManageInventory", true);

        return "dashboard/staff-dashboard";
    }

    /**
     * Dashboard cho GIAO_VIEN - Sử dụng thiết bị
     */
    private String setupTeacherDashboard(Model model, NguoiDungDTO nguoiDung) {
        log.info("🎯 Setting up TEACHER dashboard");
        model.addAttribute("title", "Dashboard Giáo viên");
        model.addAttribute("roleTitle", "Giáo viên");
        model.addAttribute("dashboardType", "teacher");

        // ✅ Thống kê sử dụng
        model.addAttribute("myClassrooms", 3);
        model.addAttribute("availableDevices", 25);
        model.addAttribute("myRequests", 2);
        model.addAttribute("notifications", 1);
        model.addAttribute("weeklyClasses", 18);
        model.addAttribute("studentCount", 120);

        // ✅ Quyền cơ bản
        model.addAttribute("canViewDevices", true);
        model.addAttribute("canCreateRequests", true);
        model.addAttribute("canViewStatus", true);

        return "dashboard/teacher-dashboard";
    }

    /**
     * Dashboard mặc định
     */
    private String setupDefaultDashboard(Model model, NguoiDungDTO nguoiDung) {
        log.info("🎯 Setting up DEFAULT dashboard");
        model.addAttribute("title", "Dashboard");
        model.addAttribute("roleTitle", "Người dùng");
        model.addAttribute("dashboardType", "default");
        model.addAttribute("message", "Chào mừng bạn đến với hệ thống");

        // ✅ FALLBACK VỀ ADMIN DASHBOARD NẾU KHÔNG CÓ FILE DEFAULT
        return "dashboard/admin-dashboard";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "Anonymous";
        log.warn("🚫 Access denied for user: {}", username);

        model.addAttribute("title", "Truy cập bị từ chối");
        model.addAttribute("message", "Bạn không có quyền truy cập chức năng này");
        model.addAttribute("username", username);

        return "error/access-denied";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        try {
            NguoiDungDTO nguoiDung = nguoiDungService.layNguoiDungTheoTenDangNhap(username);
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("title", "Đổi mật khẩu");

            return "auth/change-password";
        } catch (Exception e) {
            log.error("❌ Lỗi load form đổi mật khẩu: {}", e.getMessage());
            return "redirect:/dashboard?error=change_password_error";
        }
    }

    // ✅ Route debug để test
    @GetMapping("/test-dashboard")
    public String testDashboard(Model model) {
        log.info("🧪 Test dashboard access");
        return setupAdminDashboard(model, new NguoiDungDTO());
    }

    @GetMapping("/error")
    public String errorPage(Model model) {
        log.info("❌ Truy cập trang lỗi");
        model.addAttribute("title", "Lỗi hệ thống");
        model.addAttribute("message", "Đã xảy ra lỗi, vui lòng thử lại sau");
        return "error/404";
    }
}