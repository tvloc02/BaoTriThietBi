package com.hethong.baotri.thuc_the.thiet_bi;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Thực thể thiết bị trong hệ thống
 *
 * @author Đội phát triển hệ thống bảo trì
 * @version 1.0
 */
@Entity
@Table(name = "thiet_bi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"nhomThietBi", "thongSoThietBiSet", "lichSuBaoTriSet"})
public class ThietBi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thiet_bi")
    private Long idThietBi;

    @NotBlank(message = "Mã thiết bị không được để trống")
    @Size(max = 50, message = "Mã thiết bị không được vượt quá 50 ký tự")
    @Column(name = "ma_thiet_bi", unique = true, nullable = false, length = 50)
    private String maThietBi;

    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(max = 200, message = "Tên thiết bị không được vượt quá 200 ký tự")
    @Column(name = "ten_thiet_bi", nullable = false, length = 200)
    private String tenThietBi;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    @Column(name = "mo_ta", length = 500)
    private String moTa;

    @Size(max = 100, message = "Hãng sản xuất không được vượt quá 100 ký tự")
    @Column(name = "hang_san_xuat", length = 100)
    private String hangSanXuat;

    @Size(max = 100, message = "Model không được vượt quá 100 ký tự")
    @Column(name = "model", length = 100)
    private String model;

    @Size(max = 50, message = "Số serial không được vượt quá 50 ký tự")
    @Column(name = "so_serial", length = 50)
    private String soSerial;

    @Column(name = "nam_san_xuat")
    private Integer namSanXuat;

    @Column(name = "ngay_lap_dat")
    private LocalDate ngayLapDat;

    @Column(name = "ngay_dua_vao_su_dung")
    private LocalDate ngayDuaVaoSuDung;

    @Column(name = "ngay_bao_hanh_het_han")
    private LocalDate ngayBaoHanhHetHan;

    @Column(name = "gia_tri_ban_dau", precision = 15, scale = 2)
    private BigDecimal giaTriBanDau;

    @Column(name = "gia_tri_hien_tai", precision = 15, scale = 2)
    private BigDecimal giaTriHienTai;

    @Column(name = "thoi_gian_bao_tri_du_kien")
    private Integer thoiGianBaoTriDuKien; // Tính bằng giờ

    @Column(name = "chu_ky_bao_tri_dinh_ky")
    private Integer chuKyBaoTriDinhKy; // Tính bằng ngày

    @Size(max = 200, message = "Vị trí lắp đặt không được vượt quá 200 ký tự")
    @Column(name = "vi_tri_lap_dat", length = 200)
    private String viTriLapDat;

    @Column(name = "cong_suat_danh_dinh", precision = 10, scale = 2)
    private BigDecimal congSuatDanhDinh;

    @Size(max = 20, message = "Đơn vị công suất không được vượt quá 20 ký tự")
    @Column(name = "don_vi_cong_suat", length = 20)
    private String donViCongSuat;

    @Column(name = "dien_ap_hoat_dong", precision = 10, scale = 2)
    private BigDecimal dienApHoatDong;

    @Column(name = "dong_dien_hoat_dong", precision = 10, scale = 2)
    private BigDecimal dongDienHoatDong;

    @Column(name = "nhiet_do_hoat_dong_min", precision = 5, scale = 2)
    private BigDecimal nhietDoHoatDongMin;

    @Column(name = "nhiet_do_hoat_dong_max", precision = 5, scale = 2)
    private BigDecimal nhietDoHoatDongMax;

    @Column(name = "do_am_hoat_dong_min", precision = 5, scale = 2)
    private BigDecimal doAmHoatDongMin;

    @Column(name = "do_am_hoat_dong_max", precision = 5, scale = 2)
    private BigDecimal doAmHoatDongMax;

    @Column(name = "can_nang", precision = 10, scale = 2)
    private BigDecimal canNang;

    @Size(max = 20, message = "Đơn vị cân nặng không được vượt quá 20 ký tự")
    @Column(name = "don_vi_can_nang", length = 20)
    private String donViCanNang;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    @Column(name = "ghi_chu", length = 500)
    private String ghiChu;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "trang_thai_hoat_dong", nullable = false)
    private Boolean trangThaiHoatDong = true;

    @Column(name = "lan_bao_tri_cuoi")
    private LocalDateTime lanBaoTriCuoi;

    @Column(name = "lan_bao_tri_tiep_theo")
    private LocalDateTime lanBaoTriTiepTheo;

    @Column(name = "so_gio_hoat_dong", nullable = false)
    private Integer soGioHoatDong = 0;

    @Column(name = "so_lan_bao_tri", nullable = false)
    private Integer soLanBaoTri = 0;

    @Column(name = "tong_thoi_gian_dung_may", nullable = false)
    private Integer tongThoiGianDungMay = 0; // Tính bằng phút

    @Column(name = "tong_thoi_gian_sua_chua", nullable = false)
    private Integer tongThoiGianSuaChua = 0; // Tính bằng phút

    // Quan hệ với NhomThietBi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhom_thiet_bi", nullable = false)
    @NotNull(message = "Nhóm thiết bị không được để trống")
    private NhomThietBi nhomThietBi;

    // Quan hệ với TrangThaiThietBi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trang_thai", nullable = false)
    @NotNull(message = "Trạng thái thiết bị không được để trống")
    private TrangThaiThietBi trangThaiThietBi;

    // Quan hệ với DoiBaoTri
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_doi_bao_tri")
    private com.hethong.baotri.thuc_the.doi_bao_tri.DoiBaoTri doiBaoTriPhuTrach;

    // Quan hệ với NguoiDung (người phụ trách)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_phu_trach")
    private com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung nguoiPhuTrach;

    // Quan hệ One-to-Many với ThongSoThietBi
    @OneToMany(mappedBy = "thietBi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ThongSoThietBi> thongSoThietBiSet = new HashSet<>();

    // Quan hệ One-to-Many với YeuCauBaoTri
    @OneToMany(mappedBy = "thietBi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<com.hethong.baotri.thuc_the.bao_tri.YeuCauBaoTri> yeuCauBaoTriSet = new HashSet<>();

    // Quan hệ Many-to-Many với VatTu (vật tư liên quan)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "thiet_bi_vat_tu",
            joinColumns = @JoinColumn(name = "id_thiet_bi"),
            inverseJoinColumns = @JoinColumn(name = "id_vat_tu")
    )
    private Set<com.hethong.baotri.thuc_the.vat_tu.VatTu> vatTuLienQuanSet = new HashSet<>();

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
     * Tính toán MTBF (Mean Time Between Failures)
     *
     * @return MTBF tính bằng giờ
     */
    public double tinhMTBF() {
        if (soLanBaoTri == 0) return 0.0;
        return (double) soGioHoatDong / soLanBaoTri;
    }

    /**
     * Tính toán MTTR (Mean Time To Repair)
     *
     * @return MTTR tính bằng phút
     */
    public double tinhMTTR() {
        if (soLanBaoTri == 0) return 0.0;
        return (double) tongThoiGianSuaChua / soLanBaoTri;
    }

    /**
     * Tính toán tỷ lệ khả dụng (Availability)
     *
     * @return tỷ lệ khả dụng (0-1)
     */
    public double tinhTyLeKhaDung() {
        int tongThoiGian = soGioHoatDong * 60 + tongThoiGianDungMay + tongThoiGianSuaChua;
        if (tongThoiGian == 0) return 0.0;
        return (double) (soGioHoatDong * 60) / tongThoiGian;
    }

    /**
     * Tính toán OEE (Overall Equipment Effectiveness)
     *
     * @return OEE tính bằng phần trăm
     */
    public double tinhOEE() {
        // Công thức đơn giản: OEE = Availability × Performance × Quality
        // Hiện tại chỉ tính Availability, có thể mở rộng sau
        return tinhTyLeKhaDung() * 100;
    }

    /**
     * Kiểm tra thiết bị có cần bảo trì không
     *
     * @return true nếu cần bảo trì, false nếu không
     */
    public boolean canBaoTri() {
        if (lanBaoTriTiepTheo == null) return false;
        return lanBaoTriTiepTheo.isBefore(LocalDateTime.now());
    }

    /**
     * Cập nhật thông tin sau khi bảo trì
     *
     * @param thoiGianBaoTri thời gian bảo trì (phút)
     */
    public void capNhatSauBaoTri(int thoiGianBaoTri) {
        this.lanBaoTriCuoi = LocalDateTime.now();
        this.soLanBaoTri++;
        this.tongThoiGianSuaChua += thoiGianBaoTri;

        // Tính toán lần bảo trì tiếp theo
        if (chuKyBaoTriDinhKy != null) {
            this.lanBaoTriTiepTheo = LocalDateTime.now().plusDays(chuKyBaoTriDinhKy);
        }
    }

    /**
     * Cập nhật số giờ hoạt động
     *
     * @param gioHoatDong số giờ hoạt động thêm
     */
    public void capNhatGioHoatDong(int gioHoatDong) {
        this.soGioHoatDong += gioHoatDong;
    }

    /**
     * Cập nhật thời gian dừng máy
     *
     * @param thoiGianDungMay thời gian dừng máy (phút)
     */
    public void capNhatThoiGianDungMay(int thoiGianDungMay) {
        this.tongThoiGianDungMay += thoiGianDungMay;
    }

    /**
     * Lấy tuổi thiết bị tính bằng năm
     *
     * @return tuổi thiết bị
     */
    public int getTuoiThietBi() {
        if (ngayDuaVaoSuDung == null) return 0;
        return LocalDate.now().getYear() - ngayDuaVaoSuDung.getYear();
    }

    /**
     * Kiểm tra thiết bị có còn bảo hành không
     *
     * @return true nếu còn bảo hành, false nếu không
     */
    public boolean conBaoHanh() {
        if (ngayBaoHanhHetHan == null) return false;
        return ngayBaoHanhHetHan.isAfter(LocalDate.now());
    }

    /**
     * Tính số ngày đến lần bảo trì tiếp theo
     *
     * @return số ngày, âm nếu đã quá hạn
     */
    public long getSoNgayDenBaoTriTiepTheo() {
        if (lanBaoTriTiepTheo == null) return Long.MAX_VALUE;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDateTime.now(), lanBaoTriTiepTheo);
    }
}