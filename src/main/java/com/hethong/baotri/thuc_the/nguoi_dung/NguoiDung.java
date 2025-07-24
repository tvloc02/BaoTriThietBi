package com.hethong.baotri.thuc_the.nguoi_dung;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Thực thể người dùng trong hệ thống
 *
 * @author Đội phát triển hệ thống bảo trì
 * @version 1.0
 */
@Entity
@Table(name = "nguoi_dung")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"matKhau", "vaiTroSet"})
public class NguoiDung implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nguoi_dung")
    private Long idNguoiDung;

    // Thêm alias getId()
    public Long getId() {
        return getIdNguoiDung();
    }

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3-50 ký tự")
    @Column(name = "ten_dang_nhap", unique = true, nullable = false, length = 50)
    private String tenDangNhap;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 100, message = "Họ và tên không được vượt quá 100 ký tự")
    @Column(name = "ho_va_ten", nullable = false, length = 100)
    private String hoVaTen;

    @Email(message = "Email không đúng định dạng")
    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Size(max = 15, message = "Số điện thoại không được vượt quá 15 ký tự")
    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    @Size(max = 200, message = "Địa chỉ không được vượt quá 200 ký tự")
    @Column(name = "dia_chi", length = 200)
    private String diaChi;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "lan_dang_nhap_cuoi")
    private LocalDateTime lanDangNhapCuoi;

    @Column(name = "trang_thai_hoat_dong", nullable = false)
    private Boolean trangThaiHoatDong = true;

    @Column(name = "tai_khoan_khong_het_han", nullable = false)
    private Boolean taiKhoanKhongHetHan = true;

    @Column(name = "tai_khoan_khong_bi_khoa", nullable = false)
    private Boolean taiKhoanKhongBiKhoa = true;

    @Column(name = "thong_tin_dang_nhap_hop_le", nullable = false)
    private Boolean thongTinDangNhapHopLe = true;

    @Column(name = "so_lan_dang_nhap_that_bai", nullable = false)
    private Integer soLanDangNhapThatBai = 0;

    @Column(name = "thoi_gian_khoa_tai_khoan")
    private LocalDateTime thoiGianKhoaTaiKhoan;

    // Quan hệ Many-to-Many với VaiTro
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "nguoi_dung_vai_tro",
            joinColumns = @JoinColumn(name = "id_nguoi_dung"),
            inverseJoinColumns = @JoinColumn(name = "id_vai_tro")
    )
    private Set<VaiTro> vaiTroSet = new HashSet<>();

    // Quan hệ với DoiBaoTri
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_doi_bao_tri")
    private com.hethong.baotri.thuc_the.doi_bao_tri.DoiBaoTri doiBaoTri;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }

    /**
     * Phương thức thêm vai trò cho người dùng
     *
     * @param vaiTro vai trò cần thêm
     */
    public void themVaiTro(VaiTro vaiTro) {
        this.vaiTroSet.add(vaiTro);
        vaiTro.getNguoiDungSet().add(this);
    }

    /**
     * Phương thức xóa vai trò khỏi người dùng
     *
     * @param vaiTro vai trò cần xóa
     */
    public void xoaVaiTro(VaiTro vaiTro) {
        this.vaiTroSet.remove(vaiTro);
        vaiTro.getNguoiDungSet().remove(this);
    }

    /**
     * Kiểm tra người dùng có vai trò cụ thể hay không
     *
     * @param tenVaiTro tên vai trò cần kiểm tra
     * @return true nếu có vai trò, false nếu không
     */
    public boolean coVaiTro(String tenVaiTro) {
        return vaiTroSet.stream()
                .anyMatch(vaiTro -> vaiTro.getTenVaiTro().equals(tenVaiTro));
    }

    /**
     * Cập nhật thông tin đăng nhập cuối
     */
    public void capNhatLanDangNhapCuoi() {
        this.lanDangNhapCuoi = LocalDateTime.now();
        this.soLanDangNhapThatBai = 0;
    }

    /**
     * Tăng số lần đăng nhập thất bại
     */
    public void tangSoLanDangNhapThatBai() {
        this.soLanDangNhapThatBai++;
        if (this.soLanDangNhapThatBai >= 5) {
            this.taiKhoanKhongBiKhoa = false;
            this.thoiGianKhoaTaiKhoan = LocalDateTime.now().plusMinutes(15);
        }
    }

    /**
     * Mở khóa tài khoản
     */
    public void moKhoaTaiKhoan() {
        this.taiKhoanKhongBiKhoa = true;
        this.soLanDangNhapThatBai = 0;
        this.thoiGianKhoaTaiKhoan = null;
    }

    // Implement UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return vaiTroSet.stream()
                .flatMap(vaiTro -> vaiTro.getQuyenSet().stream())
                .map(quyen -> new SimpleGrantedAuthority(quyen.getTenQuyen()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.matKhau;
    }

    @Override
    public String getUsername() {
        return this.tenDangNhap;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.taiKhoanKhongHetHan;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (thoiGianKhoaTaiKhoan != null && thoiGianKhoaTaiKhoan.isAfter(LocalDateTime.now())) {
            return false;
        }
        return this.taiKhoanKhongBiKhoa;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.thongTinDangNhapHopLe;
    }

    @Override
    public boolean isEnabled() {
        return this.trangThaiHoatDong;
    }
}