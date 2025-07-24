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
        log.info("Truy cập trang chủ - chuyển hướng về dashboard");
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String dangNhap(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        log.info("Hiển thị trang đăng nhập");

        if (error != null) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
            log.warn("Đăng nhập thất bại");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Đăng xuất thành công!");
            log.info("Đăng xuất thành công");
        }

        return "nguoi-dung/dang-nhap";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("Truy cập dashboard");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            model.addAttribute("username", auth.getName());
            model.addAttribute("authorities", auth.getAuthorities());
            log.info("Dashboard - User: {}, Authorities: {}", auth.getName(), auth.getAuthorities());
            return "dashboard";
        } else {
            log.warn("Chưa đăng nhập, chuyển về login");
            return "redirect:/login";
        }
    }

    @GetMapping("/error")
    public String errorPage() {
        log.info("Truy cập trang lỗi");
        return "error/404"; // Sử dụng trang lỗi có sẵn
    }
}