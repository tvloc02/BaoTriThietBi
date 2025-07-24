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
            model.addAttribute("tenDangNhap", auth.getName());
            model.addAttribute("authorities", auth.getAuthorities());

            // ✅ THÊM: Các thông tin cần thiết cho dashboard
            model.addAttribute("totalUsers", "40,689");
            model.addAttribute("totalOrders", "10,293");
            model.addAttribute("totalSales", "$89,000");
            model.addAttribute("totalPending", "2,040");
            model.addAttribute("notifications", 3);

            log.info("Dashboard - User: {}, Authorities: {}", auth.getName(), auth.getAuthorities());
            return "dashboard";
        } else {
            log.warn("Chưa đăng nhập, chuyển về login");
            return "redirect:/login";
        }
    }

    // ✅ THÊM: Các route cho menu navigation
    @GetMapping("/nguoi-dung")
    public String nguoiDung(Model model) {
        return "nguoi-dung/danh-sach-nguoi-dung";
    }

    @GetMapping("/nguoi-dung/them")
    public String themNguoiDung(Model model) {
        return "nguoi-dung/them-nguoi-dung";
    }

    @GetMapping("/nguoi-dung/phan-quyen")
    public String phanQuyen(Model model) {
        return "nguoi-dung/phan-quyen";
    }

    @GetMapping("/thiet-bi")
    public String thietBi(Model model) {
        return "thiet-bi/danh-sach-thiet-bi";
    }

    @GetMapping("/thiet-bi/them")
    public String themThietBi(Model model) {
        return "thiet-bi/them-thiet-bi";
    }

    @GetMapping("/thiet-bi/phan-cong")
    public String phanCongThietBi(Model model) {
        return "thiet-bi/phan-cong-thiet-bi";
    }

    @GetMapping("/vat-tu")
    public String vatTu(Model model) {
        return "vat-tu/danh-sach-vat-tu";
    }

    @GetMapping("/vat-tu/them")
    public String themVatTu(Model model) {
        return "vat-tu/them-vat-tu";
    }

    @GetMapping("/vat-tu/quan-ly-kho")
    public String quanLyKho(Model model) {
        return "vat-tu/quan-ly-kho";
    }

    @GetMapping("/bao-tri/ke-hoach")
    public String keHoachBaoTri(Model model) {
        return "bao-tri/ke-hoach-bao-tri";
    }

    @GetMapping("/bao-tri/yeu-cau")
    public String yeuCauBaoTri(Model model) {
        return "bao-tri/yeu-cau-bao-tri";
    }

    @GetMapping("/bao-tri/thuc-hien")
    public String thucHienBaoTri(Model model) {
        return "bao-tri/thuc-hien-bao-tri";
    }

    @GetMapping("/bao-tri/kiem-tra-dinh-ky")
    public String kiemTraDinhKy(Model model) {
        return "bao-tri/kiem-tra-dinh-ky";
    }

    @GetMapping("/bao-tri/canh-bao")
    public String canhBaoLoi(Model model) {
        return "bao-tri/canh-bao-loi";
    }

    @GetMapping("/doi-bao-tri")
    public String doiBaoTri(Model model) {
        return "doi-bao-tri/danh-sach-doi";
    }

    @GetMapping("/doi-bao-tri/them")
    public String themDoi(Model model) {
        return "doi-bao-tri/them-doi";
    }

    @GetMapping("/doi-bao-tri/phan-cong")
    public String phanCongCongViec(Model model) {
        return "doi-bao-tri/phan-cong-cong-viec";
    }

    @GetMapping("/san-xuat/thong-tin")
    public String thongTinSanXuat(Model model) {
        return "san-xuat/thong-tin-san-xuat";
    }

    @GetMapping("/san-xuat/hieu-nang")
    public String hieuNangThietBi(Model model) {
        return "san-xuat/hieu-nang-thiet-bi";
    }

    @GetMapping("/bao-cao/oee")
    public String baoCaoOEE(Model model) {
        return "bao-cao/bao-cao-oee";
    }

    @GetMapping("/bao-cao/mtbf")
    public String baoCaoMTBF(Model model) {
        return "bao-cao/bao-cao-mtbf";
    }

    @GetMapping("/bao-cao/tong-hop")
    public String baoCaoTongHop(Model model) {
        return "bao-cao/bao-cao-tong-hop";
    }

    @GetMapping("/bao-cao/thong-ke")
    public String thongKe(Model model) {
        return "bao-cao/thong-ke";
    }

    @GetMapping("/error")
    public String errorPage() {
        log.info("Truy cập trang lỗi");
        return "error/404";
    }

    // ✅ THÊM: Test route để debug
    @GetMapping("/test-dashboard")
    public String testDashboard(Model model) {
        model.addAttribute("tenDangNhap", "test-user");
        model.addAttribute("authorities", "ROLE_ADMIN");
        return "dashboard";
    }
}