package com.hethong.baotri.dieu_khien;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TrangChuWebController {



    @GetMapping("/trang-chu")
    public String dashboard(Model model) {
        // Thêm dữ liệu mẫu cho dashboard
        model.addAttribute("title", "Dashboard - Hệ thống quản lý bảo trì");
        model.addAttribute("totalUsers", "1,245");
        model.addAttribute("totalOrders", "2,890");
        model.addAttribute("totalSales", "₫850,000,000");
        model.addAttribute("totalPending", "45");
        model.addAttribute("notifications", 3);
        model.addAttribute("vatTuThieuHang", 5);
        model.addAttribute("yeuCauChoDuyet", 8);

        return "layout/trang-chu";
    }

    @GetMapping("/login")
    public String dangnhap() {
        return "nguoi-dung/dang-nhap";
    }

    @GetMapping("/logout")
    public String dangxuat() {
        return "redirect:/login";
    }
}