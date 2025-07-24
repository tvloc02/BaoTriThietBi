

-- 1. BẢNG QUYỀN (QUYEN)
CREATE TABLE quyen (
                       id_quyen BIGINT AUTO_INCREMENT PRIMARY KEY,
                       ten_quyen VARCHAR(100) UNIQUE NOT NULL,
                       mo_ta VARCHAR(255),
                       nhom_quyen VARCHAR(50),
                       trang_thai_hoat_dong BOOLEAN NOT NULL DEFAULT TRUE,
                       ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. BẢNG VAI TRÒ (VAI_TRO)
CREATE TABLE vai_tro (
                         id_vai_tro BIGINT AUTO_INCREMENT PRIMARY KEY,
                         ten_vai_tro VARCHAR(50) UNIQUE NOT NULL,
                         mo_ta VARCHAR(255),
                         trang_thai_hoat_dong BOOLEAN NOT NULL DEFAULT TRUE,
                         ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. BẢNG TRẠNG THÁI THIẾT BỊ (TRANG_THAI_THIET_BI)
CREATE TABLE trang_thai_thiet_bi (
                                     id_trang_thai BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     ma_trang_thai VARCHAR(50) UNIQUE NOT NULL,
                                     ten_trang_thai VARCHAR(100) NOT NULL,
                                     mo_ta VARCHAR(255),
                                     mau_sac VARCHAR(20),
                                     bieu_tuong VARCHAR(50),
                                     cho_phep_hoat_dong BOOLEAN DEFAULT FALSE,
                                     yeu_cau_bao_tri BOOLEAN DEFAULT FALSE,
                                     trang_thai_hoat_dong BOOLEAN NOT NULL DEFAULT TRUE
);

-- 4. BẢNG NHÓM THIẾT BỊ (NHOM_THIET_BI)
CREATE TABLE nhom_thiet_bi (
                               id_nhom_thiet_bi BIGINT AUTO_INCREMENT PRIMARY KEY,
                               ma_nhom VARCHAR(50) UNIQUE NOT NULL,
                               ten_nhom VARCHAR(100) NOT NULL,
                               mo_ta VARCHAR(255),
                               cap_do INT DEFAULT 1,
                               thu_tu_hien_thi INT DEFAULT 0,
                               id_nhom_cha BIGINT,
                               trang_thai_hoat_dong BOOLEAN NOT NULL DEFAULT TRUE,
                               FOREIGN KEY (id_nhom_cha) REFERENCES nhom_thiet_bi(id_nhom_thiet_bi)
);

-- 5. BẢNG ĐỘI BẢO TRÌ (DOI_BAO_TRI)
CREATE TABLE doi_bao_tri (
                             id_doi_bao_tri BIGINT AUTO_INCREMENT PRIMARY KEY,
                             ma_doi VARCHAR(50) UNIQUE NOT NULL,
                             ten_doi VARCHAR(100) NOT NULL,
                             mo_ta VARCHAR(255),
                             chuyen_mon VARCHAR(100),
                             khu_vuc_hoat_dong VARCHAR(100),
                             ca_lam_viec VARCHAR(50),
                             so_thanh_vien_toi_da INT DEFAULT 10,
                             so_thanh_vien_hien_tai INT DEFAULT 0,
                             trang_thai_hoat_dong BOOLEAN NOT NULL DEFAULT TRUE,
                             ngay_thanh_lap TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             id_truong_doi BIGINT
);

-- 6. BẢNG NGƯỜI DÙNG (NGUOI_DUNG)
CREATE TABLE nguoi_dung (
                            id_nguoi_dung BIGINT AUTO_INCREMENT PRIMARY KEY,
                            ten_dang_nhap VARCHAR(50) UNIQUE NOT NULL,
                            mat_khau VARCHAR(255) NOT NULL,
                            ho_va_ten VARCHAR(100) NOT NULL,
                            email VARCHAR(100) UNIQUE NOT NULL,
                            so_dien_thoai VARCHAR(20),
                            dia_chi VARCHAR(255),
                            trang_thai_hoat_dong BOOLEAN NOT NULL DEFAULT TRUE,
                            tai_khoan_khong_bi_khoa BOOLEAN NOT NULL DEFAULT TRUE,
                            so_lan_dang_nhap_that_bai INT DEFAULT 0,
                            lan_dang_nhap_cuoi TIMESTAMP,
                            ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            id_doi_bao_tri BIGINT,
                            FOREIGN KEY (id_doi_bao_tri) REFERENCES doi_bao_tri(id_doi_bao_tri)
);

-- 7. BẢNG VAI TRÒ - QUYỀN (VAI_TRO_QUYEN)
CREATE TABLE vai_tro_quyen (
                               id_vai_tro BIGINT NOT NULL,
                               id_quyen BIGINT NOT NULL,
                               PRIMARY KEY (id_vai_tro, id_quyen),
                               FOREIGN KEY (id_vai_tro) REFERENCES vai_tro(id_vai_tro) ON DELETE CASCADE,
                               FOREIGN KEY (id_quyen) REFERENCES quyen(id_quyen) ON DELETE CASCADE
);

-- 8. BẢNG NGƯỜI DÙNG - VAI TRÒ (NGUOI_DUNG_VAI_TRO)
CREATE TABLE nguoi_dung_vai_tro (
                                    id_nguoi_dung BIGINT NOT NULL,
                                    id_vai_tro BIGINT NOT NULL,
                                    PRIMARY KEY (id_nguoi_dung, id_vai_tro),
                                    FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id_nguoi_dung) ON DELETE CASCADE,
                                    FOREIGN KEY (id_vai_tro) REFERENCES vai_tro(id_vai_tro) ON DELETE CASCADE
);

-- 9. BẢNG THIẾT BỊ (THIET_BI)
CREATE TABLE thiet_bi (
                          id_thiet_bi BIGINT AUTO_INCREMENT PRIMARY KEY,
                          ma_thiet_bi VARCHAR(50) UNIQUE NOT NULL,
                          ten_thiet_bi VARCHAR(100) NOT NULL,
                          mo_ta TEXT,
                          hang_san_xuat VARCHAR(100),
                          model VARCHAR(100),
                          so_serial VARCHAR(100),
                          nam_san_xuat INT,
                          ngay_lap_dat DATE,
                          ngay_dua_vao_su_dung DATE,
                          gia_tri_ban_dau DECIMAL(15,2),
                          gia_tri_hien_tai DECIMAL(15,2),
                          vi_tri_lap_dat VARCHAR(255),
                          chu_ky_bao_tri_dinh_ky INT,
                          lan_bao_tri_cuoi TIMESTAMP,
                          lan_bao_tri_tiep_theo TIMESTAMP,
                          so_gio_hoat_dong DECIMAL(10,2) DEFAULT 0,
                          so_lan_bao_tri INT DEFAULT 0,
                          trang_thai_hoat_dong BOOLEAN NOT NULL DEFAULT TRUE,
                          id_nhom_thiet_bi BIGINT,
                          id_trang_thai BIGINT,
                          id_nguoi_phu_trach BIGINT,
                          id_doi_bao_tri BIGINT,
                          FOREIGN KEY (id_nhom_thiet_bi) REFERENCES nhom_thiet_bi(id_nhom_thiet_bi),
                          FOREIGN KEY (id_trang_thai) REFERENCES trang_thai_thiet_bi(id_trang_thai),
                          FOREIGN KEY (id_nguoi_phu_trach) REFERENCES nguoi_dung(id_nguoi_dung),
                          FOREIGN KEY (id_doi_bao_tri) REFERENCES doi_bao_tri(id_doi_bao_tri)
);

-- 10. BẢNG NHÓM VẬT TƯ (NHOM_VAT_TU)
CREATE TABLE nhom_vat_tu (
                             id_nhom_vat_tu BIGINT AUTO_INCREMENT PRIMARY KEY,
                             ma_nhom VARCHAR(50) UNIQUE NOT NULL,
                             ten_nhom VARCHAR(100) NOT NULL,
                             mo_ta VARCHAR(255),
                             cap_do INT DEFAULT 1,
                             thu_tu_hien_thi INT DEFAULT 0,
                             id_nhom_cha BIGINT,
                             trang_thai_hoat_dong BOOLEAN NOT NULL DEFAULT TRUE,
                             FOREIGN KEY (id_nhom_cha) REFERENCES nhom_vat_tu(id_nhom_vat_tu)
);

-- 11. BẢNG VẬT TƯ (VAT_TU)
CREATE TABLE vat_tu (
                        id_vat_tu BIGINT AUTO_INCREMENT PRIMARY KEY,
                        ma_vat_tu VARCHAR(50) UNIQUE NOT NULL,
                        ten_vat_tu VARCHAR(100) NOT NULL,
                        mo_ta TEXT,
                        don_vi_tinh VARCHAR(20),
                        gia_nhap DECIMAL(12,2),
                        gia_xuat DECIMAL(12,2),
                        so_luong_ton_kho INT DEFAULT 0,
                        so_luong_ton_toi_thieu INT DEFAULT 0,
                        so_luong_ton_toi_da INT DEFAULT 1000,
                        muc_do_quan_trong INT DEFAULT 1,
                        loai_vat_tu VARCHAR(50),
                        hang_san_xuat VARCHAR(100),
                        model VARCHAR(100),
                        co_the_thay_the BOOLEAN DEFAULT FALSE,
                        vat_tu_quan_trong BOOLEAN DEFAULT FALSE,
                        trang_thai_hoat_dong BOOLEAN NOT NULL DEFAULT TRUE,
                        id_nhom_vat_tu BIGINT,
                        FOREIGN KEY (id_nhom_vat_tu) REFERENCES nhom_vat_tu(id_nhom_vat_tu)
);

-- 12. BẢNG KẾ HOẠCH BẢO TRÌ (KE_HOACH_BAO_TRI)
CREATE TABLE ke_hoach_bao_tri (
                                  id_ke_hoach BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  ma_ke_hoach VARCHAR(50) UNIQUE NOT NULL,
                                  ten_ke_hoach VARCHAR(100) NOT NULL,
                                  mo_ta TEXT,
                                  loai_ke_hoach VARCHAR(50),
                                  ngay_bat_dau TIMESTAMP,
                                  ngay_ket_thuc TIMESTAMP,
                                  chu_ky_lap_lai INT,
                                  trang_thai VARCHAR(50) DEFAULT 'DRAFT',
                                  ty_le_hoan_thanh DECIMAL(5,2) DEFAULT 0,
                                  id_nguoi_tao BIGINT,
                                  FOREIGN KEY (id_nguoi_tao) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 13. BẢNG YÊU CẦU BẢO TRÌ (YEU_CAU_BAO_TRI)
CREATE TABLE yeu_cau_bao_tri (
                                 id_yeu_cau BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 ma_yeu_cau VARCHAR(50) UNIQUE NOT NULL,
                                 tieu_de VARCHAR(200) NOT NULL,
                                 mo_ta TEXT,
                                 loai_yeu_cau VARCHAR(50),
                                 muc_do_uu_tien INT DEFAULT 1,
                                 trang_thai VARCHAR(50) DEFAULT 'CHO_DUYET',
                                 ngay_yeu_cau TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 ngay_mong_muon TIMESTAMP,
                                 thoi_gian_du_kien INT,
                                 chi_phi_du_kien DECIMAL(12,2),
                                 yeu_cau_dung_may BOOLEAN DEFAULT FALSE,
                                 co_anh_huong_san_xuat BOOLEAN DEFAULT FALSE,
                                 can_vat_tu_dac_biet BOOLEAN DEFAULT FALSE,
                                 ghi_chu TEXT,
                                 id_thiet_bi BIGINT,
                                 id_nguoi_yeu_cau BIGINT,
                                 id_ke_hoach BIGINT,
                                 FOREIGN KEY (id_thiet_bi) REFERENCES thiet_bi(id_thiet_bi),
                                 FOREIGN KEY (id_nguoi_yeu_cau) REFERENCES nguoi_dung(id_nguoi_dung),
                                 FOREIGN KEY (id_ke_hoach) REFERENCES ke_hoach_bao_tri(id_ke_hoach)
);

-- 14. BẢNG KHO VẬT TƯ (KHO_VAT_TU)
CREATE TABLE kho_vat_tu (
                            id_kho_vat_tu BIGINT AUTO_INCREMENT PRIMARY KEY,
                            ma_kho VARCHAR(50) UNIQUE NOT NULL,
                            ten_kho VARCHAR(100) NOT NULL,
                            mo_ta VARCHAR(255),
                            vi_tri VARCHAR(255),
                            so_luong_ton INT DEFAULT 0,
                            so_luong_kha_dung INT DEFAULT 0,
                            gia_tri_ton_kho DECIMAL(15,2) DEFAULT 0,
                            kho_chinh BOOLEAN DEFAULT TRUE,
                            trang_thai_hoat_dong BOOLEAN NOT NULL DEFAULT TRUE,
                            id_vat_tu BIGINT,
                            id_thu_kho BIGINT,
                            FOREIGN KEY (id_vat_tu) REFERENCES vat_tu(id_vat_tu),
                            FOREIGN KEY (id_thu_kho) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 15. BẢNG MẪU PHIẾU KIỂM TRA (MAU_PHIEU_KIEM_TRA)
CREATE TABLE mau_phieu_kiem_tra (
                                    id_mau_phieu BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    ma_mau_phieu VARCHAR(50) UNIQUE NOT NULL,
                                    ten_mau_phieu VARCHAR(100) NOT NULL,
                                    mo_ta TEXT,
                                    loai_thiet_bi VARCHAR(50),
                                    loai_kiem_tra VARCHAR(50),
                                    trang_thai_hoat_dong BOOLEAN NOT NULL DEFAULT TRUE,
                                    id_nguoi_tao BIGINT,
                                    FOREIGN KEY (id_nguoi_tao) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 16. BẢNG HẠNG MỤC KIỂM TRA (HANG_MUC_KIEM_TRA)
CREATE TABLE hang_muc_kiem_tra (
                                   id_hang_muc BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   ten_hang_muc VARCHAR(100) NOT NULL,
                                   mo_ta TEXT,
                                   tieu_chi TEXT,
                                   thu_tu INT DEFAULT 1,
                                   bat_buoc BOOLEAN DEFAULT FALSE,
                                   loai_kiem_tra VARCHAR(50),
                                   diem_toi_da INT DEFAULT 100,
                                   ghi_chu TEXT,
                                   id_mau_phieu BIGINT,
                                   FOREIGN KEY (id_mau_phieu) REFERENCES mau_phieu_kiem_tra(id_mau_phieu)
);

-- 17. BẢNG KIỂM TRA ĐỊNH KỲ (KIEM_TRA_DINH_KY)
CREATE TABLE kiem_tra_dinh_ky (
                                  id_kiem_tra BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  ma_kiem_tra VARCHAR(50) UNIQUE NOT NULL,
                                  ten_kiem_tra VARCHAR(100) NOT NULL,
                                  mo_ta TEXT,
                                  loai_kiem_tra VARCHAR(50),
                                  chu_ky_kiem_tra INT,
                                  ngay_kiem_tra TIMESTAMP,
                                  ngay_kiem_tra_tiep_theo TIMESTAMP,
                                  trang_thai VARCHAR(50) DEFAULT 'CHO_THUC_HIEN',
                                  ket_qua_kiem_tra TEXT,
                                  danh_gia_tong_the VARCHAR(50),
                                  kien_nghi TEXT,
                                  thoi_gian_thuc_hien INT,
                                  chi_phi DECIMAL(12,2),
                                  yeu_cau_bao_tri BOOLEAN DEFAULT FALSE,
                                  id_thiet_bi BIGINT,
                                  id_nguoi_kiem_tra BIGINT,
                                  FOREIGN KEY (id_thiet_bi) REFERENCES thiet_bi(id_thiet_bi),
                                  FOREIGN KEY (id_nguoi_kiem_tra) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 18. BẢNG CHI TIẾT KIỂM TRA (CHI_TIET_KIEM_TRA)
CREATE TABLE chi_tiet_kiem_tra (
                                   id_chi_tiet BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   ten_hang_muc VARCHAR(100) NOT NULL,
                                   tieu_chi TEXT,
                                   ket_qua TEXT,
                                   dat_chuan BOOLEAN DEFAULT FALSE,
                                   diem_so INT DEFAULT 0,
                                   ghi_chu TEXT,
                                   id_kiem_tra BIGINT,
                                   FOREIGN KEY (id_kiem_tra) REFERENCES kiem_tra_dinh_ky(id_kiem_tra)
);

-- 19. BẢNG LỊCH SỬ BẢO TRÌ (LICH_SU_BAO_TRI)
CREATE TABLE lich_su_bao_tri (
                                 id_lich_su BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 ngay_bao_tri TIMESTAMP NOT NULL,
                                 loai_bao_tri VARCHAR(50),
                                 noi_dung TEXT,
                                 ket_qua TEXT,
                                 thoi_gian_thuc_hien INT,
                                 chi_phi DECIMAL(12,2),
                                 vat_tu_su_dung TEXT,
                                 id_thiet_bi BIGINT,
                                 id_nguoi_bao_tri BIGINT,
                                 FOREIGN KEY (id_thiet_bi) REFERENCES thiet_bi(id_thiet_bi),
                                 FOREIGN KEY (id_nguoi_bao_tri) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 20. BẢNG THÀNH VIÊN ĐỘI (THANH_VIEN_DOI)
CREATE TABLE thanh_vien_doi (
                                id_thanh_vien BIGINT AUTO_INCREMENT PRIMARY KEY,
                                chuc_vu VARCHAR(50),
                                ngay_tham_gia DATE,
                                trang_thai_hoat_dong BOOLEAN DEFAULT TRUE,
                                muc_do_kinh_nghiem INT DEFAULT 1,
                                ky_nang_chuyen_mon TEXT,
                                id_doi_bao_tri BIGINT,
                                id_nguoi_dung BIGINT,
                                FOREIGN KEY (id_doi_bao_tri) REFERENCES doi_bao_tri(id_doi_bao_tri),
                                FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 21. BẢNG LỊCH SỬ NHẬP XUẤT (LICH_SU_NHAP_XUAT)
CREATE TABLE lich_su_nhap_xuat (
                                   id_lich_su BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   loai_giao_dich VARCHAR(10) NOT NULL,
                                   so_luong INT NOT NULL,
                                   don_gia DECIMAL(12,2),
                                   thanh_tien DECIMAL(15,2),
                                   so_luong_ton_truoc INT,
                                   so_luong_ton_sau INT,
                                   ngay_giao_dich TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   ly_do VARCHAR(255),
                                   ghi_chu TEXT,
                                   so_chung_tu VARCHAR(50),
                                   id_vat_tu BIGINT,
                                   id_kho_vat_tu BIGINT,
                                   id_nguoi_thuc_hien BIGINT,
                                   FOREIGN KEY (id_vat_tu) REFERENCES vat_tu(id_vat_tu),
                                   FOREIGN KEY (id_kho_vat_tu) REFERENCES kho_vat_tu(id_kho_vat_tu),
                                   FOREIGN KEY (id_nguoi_thuc_hien) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 22. BẢNG CẢNH BÁO LỖI (CANH_BAO_LOI)
CREATE TABLE canh_bao_loi (
                              id_canh_bao BIGINT AUTO_INCREMENT PRIMARY KEY,
                              ma_canh_bao VARCHAR(50) UNIQUE NOT NULL,
                              tieu_de VARCHAR(200) NOT NULL,
                              mo_ta TEXT,
                              loai_canh_bao VARCHAR(50),
                              muc_do_nghiem_trong INT DEFAULT 1,
                              trang_thai VARCHAR(50) DEFAULT 'CHO_XU_LY',
                              ngay_phat_sinh TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              tu_dong_tao BOOLEAN DEFAULT FALSE,
                              can_thong_bao_ngay BOOLEAN DEFAULT FALSE,
                              da_gui_thong_bao BOOLEAN DEFAULT FALSE,
                              ghi_chu TEXT,
                              id_thiet_bi BIGINT,
                              id_nguoi_xu_ly BIGINT,
                              FOREIGN KEY (id_thiet_bi) REFERENCES thiet_bi(id_thiet_bi),
                              FOREIGN KEY (id_nguoi_xu_ly) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 23. BẢNG HIỆU NĂNG THIẾT BỊ (HIEU_NANG_THIET_BI)
CREATE TABLE hieu_nang_thiet_bi (
                                    id_hieu_nang BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    ngay_do TIMESTAMP NOT NULL,
                                    thoi_gian_hoat_dong DECIMAL(8,2),
                                    thoi_gian_dung_may DECIMAL(8,2),
                                    thoi_gian_bao_tri DECIMAL(8,2),
                                    ty_le_kha_dung DECIMAL(5,2),
                                    ty_le_hieu_suat DECIMAL(5,2),
                                    ty_le_chat_luong DECIMAL(5,2),
                                    oee DECIMAL(5,2),
                                    ghi_chu TEXT,
                                    id_thiet_bi BIGINT,
                                    id_nguoi_do BIGINT,
                                    FOREIGN KEY (id_thiet_bi) REFERENCES thiet_bi(id_thiet_bi),
                                    FOREIGN KEY (id_nguoi_do) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- 24. BẢNG THÔNG SỐ THIẾT BỊ (THONG_SO_THIET_BI)
CREATE TABLE thong_so_thiet_bi (
                                   id_thong_so BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   ten_thong_so VARCHAR(100) NOT NULL,
                                   gia_tri_so DECIMAL(15,4),
                                   gia_tri_van_ban VARCHAR(255),
                                   don_vi VARCHAR(20),
                                   loai_thong_so VARCHAR(20) DEFAULT 'SO',
                                   gia_tri_min DECIMAL(15,4),
                                   gia_tri_max DECIMAL(15,4),
                                   gia_tri_mac_dinh DECIMAL(15,4),
                                   bat_buoc BOOLEAN DEFAULT FALSE,
                                   hien_thi_tren_giao_dien BOOLEAN DEFAULT TRUE,
                                   co_the_chinh_sua BOOLEAN DEFAULT TRUE,
                                   thu_tu_hien_thi INT DEFAULT 1,
                                   mo_ta TEXT,
                                   trang_thai_hoat_dong BOOLEAN DEFAULT TRUE,
                                   id_thiet_bi BIGINT,
                                   id_nguoi_tao BIGINT,
                                   FOREIGN KEY (id_thiet_bi) REFERENCES thiet_bi(id_thiet_bi),
                                   FOREIGN KEY (id_nguoi_tao) REFERENCES nguoi_dung(id_nguoi_dung)
);

-- ADD FOREIGN KEY CONSTRAINTS FOR CIRCULAR REFERENCES
ALTER TABLE doi_bao_tri ADD FOREIGN KEY (id_truong_doi) REFERENCES nguoi_dung(id_nguoi_dung);

-- =================================================================
-- KẾT THÚC TẠO BẢNG
-- =================================================================