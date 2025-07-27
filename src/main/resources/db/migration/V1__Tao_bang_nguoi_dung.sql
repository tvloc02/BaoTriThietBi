-- =================================================================
-- DỮ LIỆU MẪU HỆ THỐNG QUẢN LY THIẾT BỊ TRƯỜNG HỌC
-- XÓA PHẦN TẠO USER - CHỈ TẠO DỮ LIỆU CẤU TRÚC
-- =================================================================

-- 1. DỮ LIỆU QUYỀN (QUYEN)
INSERT INTO quyen (ten_quyen, mo_ta, nhom_quyen, trang_thai_hoat_dong) VALUES
-- Quyền quản lý thiết bị
('QUAN_LY_THIET_BI_XEM', 'Xem danh sách thiết bị', 'THIET_BI', true),
('QUAN_LY_THIET_BI_THEM', 'Thêm thiết bị mới', 'THIET_BI', true),
('QUAN_LY_THIET_BI_SUA', 'Sửa thông tin thiết bị', 'THIET_BI', true),
('QUAN_LY_THIET_BI_XOA', 'Xóa thiết bị', 'THIET_BI', true),

-- Quyền bảo trì
('TAO_YEU_CAU_BAO_TRI', 'Tạo yêu cầu bảo trì', 'BAO_TRI', true),
('DUYET_YEU_CAU_BAO_TRI', 'Duyệt yêu cầu bảo trì', 'BAO_TRI', true),
('THUC_HIEN_BAO_TRI', 'Thực hiện bảo trì', 'BAO_TRI', true),
('KIEM_TRA_DINH_KY', 'Kiểm tra định kỳ', 'BAO_TRI', true),

-- Quyền quản lý người dùng
('QUAN_LY_NGUOI_DUNG_XEM', 'Xem danh sách người dùng', 'NGUOI_DUNG', true),
('QUAN_LY_NGUOI_DUNG_THEM', 'Thêm người dùng', 'NGUOI_DUNG', true),
('QUAN_LY_NGUOI_DUNG_SUA', 'Sửa thông tin người dùng', 'NGUOI_DUNG', true),
('QUAN_LY_NGUOI_DUNG_XOA', 'Xóa người dùng', 'NGUOI_DUNG', true),

-- Quyền báo cáo
('XEM_BAO_CAO_TONG_HOP', 'Xem báo cáo tổng hợp', 'BAO_CAO', true),
('XUAT_BAO_CAO', 'Xuất báo cáo', 'BAO_CAO', true);

-- 2. DỮ LIỆU VAI TRÒ (VAI_TRO)
INSERT INTO vai_tro (ten_vai_tro, mo_ta, trang_thai_hoat_dong) VALUES
                                                                   ('QUAN_TRI_VIEN', 'Quản trị viên hệ thống', true),
                                                                   ('TRUONG_PHONG_CSVC', 'Trưởng phòng Cơ sở vật chất', true),
                                                                   ('NHAN_VIEN_CSVC', 'Nhân viên Cơ sở vật chất', true),
                                                                   ('GIAO_VIEN', 'Giáo viên sử dụng thiết bị', true),
                                                                   ('KY_THUAT_VIEN', 'Kỹ thuật viên bảo trì', true),
                                                                   ('HIEU_TRUONG', 'Hiệu trưởng', true),
                                                                   ('PHO_HIEU_TRUONG', 'Phó hiệu trưởng', true);

-- 3. TRẠNG THÁI THIẾT BỊ (TRANG_THAI_THIET_BI)
INSERT INTO trang_thai_thiet_bi (ma_trang_thai, ten_trang_thai, mo_ta, mau_sac, bieu_tuong, cho_phep_hoat_dong, yeu_cau_bao_tri, trang_thai_hoat_dong) VALUES
                                                                                                                                                           ('HOAT_DONG', 'Hoạt động bình thường', 'Thiết bị đang hoạt động tốt', 'green', 'bi-check-circle-fill', true, false, true),
                                                                                                                                                           ('BAO_TRI', 'Đang bảo trì', 'Thiết bị đang được bảo trì', 'yellow', 'bi-tools', false, true, true),
                                                                                                                                                           ('SUA_CHUA', 'Đang sửa chữa', 'Thiết bị đang được sửa chữa', 'orange', 'bi-wrench', false, true, true),
                                                                                                                                                           ('HONG', 'Hỏng', 'Thiết bị bị hỏng, cần sửa chữa', 'red', 'bi-exclamation-triangle-fill', false, true, true),
                                                                                                                                                           ('NGUNG_SU_DUNG', 'Ngừng sử dụng', 'Thiết bị tạm ngừng sử dụng', 'gray', 'bi-x-circle-fill', false, false, true),
                                                                                                                                                           ('CHO_THANH_LY', 'Chờ thanh lý', 'Thiết bị chờ thanh lý', 'black', 'bi-trash-fill', false, false, true);

-- 4. NHÓM THIẾT BỊ (NHOM_THIET_BI)
INSERT INTO nhom_thiet_bi (ma_nhom, ten_nhom, mo_ta, cap_do, thu_tu_hien_thi, trang_thai_hoat_dong) VALUES
-- Nhóm cấp 1
('PHONG_HOC', 'Thiết bị phòng học', 'Các thiết bị phục vụ giảng dạy trong phòng học', 1, 1, true),
('PHONG_LAB', 'Thiết bị phòng thí nghiệm', 'Thiết bị thí nghiệm khoa học', 1, 2, true),
('THIET_BI_IT', 'Thiết bị công nghệ thông tin', 'Máy tính, mạng, phần mềm', 1, 3, true),
('THIET_BI_VAN_PHONG', 'Thiết bị văn phòng', 'Máy in, máy photocopy, máy fax', 1, 4, true),
('HE_THONG_DIEN', 'Hệ thống điện', 'Hệ thống điện, máy phát điện', 1, 5, true),
('HE_THONG_NUOC', 'Hệ thống nước', 'Hệ thống cấp thoát nước', 1, 6, true),
('AN_NINH_AN_TOAN', 'An ninh - An toàn', 'Camera, báo cháy, an ninh', 1, 7, true);

-- 5. ĐỘI BẢO TRÌ (DOI_BAO_TRI) - Tạo trước không có truong_doi
INSERT INTO doi_bao_tri (ma_doi, ten_doi, mo_ta, chuyen_mon, khu_vuc_hoat_dong, ca_lam_viec, so_thanh_vien_toi_da, so_thanh_vien_hien_tai, trang_thai_hoat_dong, ngay_thanh_lap) VALUES
                                                                                                                                                                                     ('DBT001', 'Đội Bảo trì Thiết bị IT', 'Chuyên bảo trì thiết bị công nghệ thông tin', 'DIEN_TU', 'Toàn trường', 'CA_HANH_CHINH', 5, 0, true, '2023-01-01'),
                                                                                                                                                                                     ('DBT002', 'Đội Bảo trì Cơ điện', 'Chuyên bảo trì hệ thống điện, điều hòa', 'DIEN', 'Toàn trường', 'CA_HANH_CHINH', 4, 0, true, '2023-01-01'),
                                                                                                                                                                                     ('DBT003', 'Đội Bảo trì Tổng hợp', 'Bảo trì các thiết bị khác', 'TONG_HOP', 'Toàn trường', 'CA_HANH_CHINH', 6, 0, true, '2023-01-01');

-- 6. PHÂN QUYỀN CHO VAI TRÒ (VAI_TRO_QUYEN)
-- Admin có tất cả quyền
INSERT INTO vai_tro_quyen (id_vai_tro, id_quyen)
SELECT vt.id_vai_tro, q.id_quyen
FROM vai_tro vt, quyen q
WHERE vt.ten_vai_tro = 'QUAN_TRI_VIEN';

-- Trưởng phòng CSVC
INSERT INTO vai_tro_quyen (id_vai_tro, id_quyen)
SELECT vt.id_vai_tro, q.id_quyen
FROM vai_tro vt, quyen q
WHERE vt.ten_vai_tro = 'TRUONG_PHONG_CSVC'
  AND q.ten_quyen IN ('QUAN_LY_THIET_BI_XEM', 'QUAN_LY_THIET_BI_THEM', 'QUAN_LY_THIET_BI_SUA',
                      'DUYET_YEU_CAU_BAO_TRI', 'XEM_BAO_CAO_TONG_HOP', 'XUAT_BAO_CAO',
                      'QUAN_LY_NGUOI_DUNG_XEM');

-- Nhân viên CSVC
INSERT INTO vai_tro_quyen (id_vai_tro, id_quyen)
SELECT vt.id_vai_tro, q.id_quyen
FROM vai_tro vt, quyen q
WHERE vt.ten_vai_tro = 'NHAN_VIEN_CSVC'
  AND q.ten_quyen IN ('QUAN_LY_THIET_BI_XEM', 'QUAN_LY_THIET_BI_THEM', 'QUAN_LY_THIET_BI_SUA',
                      'TAO_YEU_CAU_BAO_TRI', 'KIEM_TRA_DINH_KY');

-- Giáo viên
INSERT INTO vai_tro_quyen (id_vai_tro, id_quyen)
SELECT vt.id_vai_tro, q.id_quyen
FROM vai_tro vt, quyen q
WHERE vt.ten_vai_tro = 'GIAO_VIEN'
  AND q.ten_quyen IN ('QUAN_LY_THIET_BI_XEM', 'TAO_YEU_CAU_BAO_TRI');

-- Kỹ thuật viên
INSERT INTO vai_tro_quyen (id_vai_tro, id_quyen)
SELECT vt.id_vai_tro, q.id_quyen
FROM vai_tro vt, quyen q
WHERE vt.ten_vai_tro = 'KY_THUAT_VIEN'
  AND q.ten_quyen IN ('QUAN_LY_THIET_BI_XEM', 'THUC_HIEN_BAO_TRI', 'KIEM_TRA_DINH_KY');

-- ✅ QUAN TRỌNG: KHÔNG TẠO USER Ở ĐÂY
-- DataLoader sẽ tự tạo admin user với password hash đúng

-- =================================================================
-- KẾT THÚC - CHỈ TẠO DỮ LIỆU CẤU TRÚC, KHÔNG TẠO USER
-- =================================================================