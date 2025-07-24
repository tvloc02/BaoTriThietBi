package com.hethong.baotri.thuc_the.bao_tri;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "yeu_cau_bao_tri")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"thietBi", "congViecBaoTriSet"})
public class YeuCauBaoTri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_yeu_cau")
    private Long idYeuCau;

    @NotBlank(message = "Mã yêu cầu không được để trống")
    @Size(max = 50, message = "Mã yêu cầu không được vượt quá 50 ký tự")
    @Column(name = "ma_yeu_cau", unique = true, nullable = false, length = 50)
    private String maYeuCau;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 200, message = "Tiêu đề không được vượt quá 200 ký tự")
    @Column(name = "tieu_de", nullable = false, length = 200)
    private String tieuDe;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    @Column(name = "mo_ta", length = 1000)
    private String moTa;

    @NotBlank(message = "Loại yêu cầu không được để trống")
    @Column(name = "loai_yeu_cau", nullable = false, length = 50)
    private String loaiYeuCau;

    @Column(name = "muc_do_uu_tien", nullable = false)
    private Integer mucDoUuTien = 1;

    @Column(name = "trang_thai", nullable = false, length = 30)
    private String trangThai = TRANG_THAI_MOI_TAO;

    @Column(name = "ngay_yeu_cau", nullable = false)
    private LocalDateTime ngayYeuCau;

    @Column(name = "ngay_mong_muon")
    private LocalDateTime ngayMongMuon;

    @Column(name = "ngay_bat_dau_thuc_hien")
    private LocalDateTime ngayBatDauThucHien;

    @Column(name = "ngay_hoan_thanh")
    private LocalDateTime ngayHoanThanh;

    @Column(name = "thoi_gian_du_kien")
    private Integer thoiGianDuKien;

    @Column(name = "thoi_gian_thuc_te")
    private Integer thoiGianThucTe;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    @Column(name = "ghi_chu", length = 500)
    private String ghiChu;

    @Size(max = 500, message = "Lý do từ chối không được vượt quá 500 ký tự")
    @Column(name = "ly_do_tu_choi", length = 500)
    private String lyDoTuChoi;

    @Column(name = "chi_phi_du_kien", precision = 15, scale = 2)
    private BigDecimal chiPhiDuKien;

    @Column(name = "chi_phi_thuc_te", precision = 15, scale = 2)
    private BigDecimal chiPhiThucTe;

    @Column(name = "yeu_cau_dung_may", nullable = false)
    private Boolean yeuCauDungMay = false;

    @Column(name = "co_anh_huong_san_xuat", nullable = false)
    private Boolean coAnhHuongSanXuat = false;

    @Column(name = "can_vat_tu_dac_biet", nullable = false)
    private Boolean canVatTuDacBiet = false;

    @Column(name = "can_nhan_vien_chuyen_mon", nullable = false)
    private Boolean canNhanVienChuyenMon = false;

    @Column(name = "da_thong_bao_quan_ly", nullable = false)
    private Boolean daThongBaoQuanLy = false;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_thiet_bi", nullable = false)
    @NotNull(message = "Thiết bị không được để trống")
    private com.hethong.baotri.thuc_the.thiet_bi.ThietBi thietBi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_yeu_cau", nullable = false)
    @NotNull(message = "Người yêu cầu không được để trống")
    private com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung nguoiYeuCau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_duyet")
    private com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung nguoiDuyet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_thuc_hien")
    private com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung nguoiThucHien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_doi_bao_tri")
    private com.hethong.baotri.thuc_the.doi_bao_tri.DoiBaoTri doiBaoTri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ke_hoach")
    private KeHoachBaoTri keHoachBaoTri;

    @OneToMany(mappedBy = "yeuCauBaoTri", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CongViecBaoTri> congViecBaoTriSet = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
        if (ngayYeuCau == null) {
            ngayYeuCau = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }

    public void duyetYeuCau(com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung nguoiDuyet) {
        this.nguoiDuyet = nguoiDuyet;
        this.trangThai = TRANG_THAI_DA_DUYET;
    }

    public void tuChoiYeuCau(com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung nguoiDuyet, String lyDo) {
        this.nguoiDuyet = nguoiDuyet;
        this.lyDoTuChoi = lyDo;
        this.trangThai = TRANG_THAI_BI_TU_CHOI;
    }

    public void batDauThucHien(com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung nguoiThucHien) {
        this.nguoiThucHien = nguoiThucHien;
        this.ngayBatDauThucHien = LocalDateTime.now();
        this.trangThai = TRANG_THAI_DANG_THUC_HIEN;
    }

    public void hoanThanh(Integer thoiGianThucTe, BigDecimal chiPhiThucTe) {
        this.thoiGianThucTe = thoiGianThucTe;
        this.chiPhiThucTe = chiPhiThucTe;
        this.ngayHoanThanh = LocalDateTime.now();
        this.trangThai = TRANG_THAI_HOAN_THANH;
    }

    public static final String TRANG_THAI_MOI_TAO = "MOI_TAO";
    public static final String TRANG_THAI_CHO_DUYET = "CHO_DUYET";
    public static final String TRANG_THAI_DA_DUYET = "DA_DUYET";
    public static final String TRANG_THAI_BI_TU_CHOI = "BI_TU_CHOI";
    public static final String TRANG_THAI_DANG_THUC_HIEN = "DANG_THUC_HIEN";
    public static final String TRANG_THAI_HOAN_THANH = "HOAN_THANH";
    public static final String TRANG_THAI_HUY = "HUY";

    public static final String LOAI_BAO_TRI_DINH_KY = "BAO_TRI_DINH_KY";
    public static final String LOAI_SUA_CHUA_KHAN_CAP = "SUA_CHUA_KHAN_CAP";
    public static final String LOAI_BAO_DUONG_PHONG_NGUA = "BAO_DUONG_PHONG_NGUA";
    public static final String LOAI_KIEM_TRA_AN_TOAN = "KIEM_TRA_AN_TOAN";
    public static final String LOAI_THAY_THE_LINH_KIEN = "THAY_THE_LINH_KIEN";
    public static final String LOAI_NAY_CAP_THIET_BI = "NAY_CAP_THIET_BI";
}