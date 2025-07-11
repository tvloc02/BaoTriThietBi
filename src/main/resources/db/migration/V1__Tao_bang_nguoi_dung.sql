-- =================================================================
-- DỮ LIỆU MẪU HỆ THỐNG QUẢN LÝ THIẾT BỊ TRƯỜNG HỌC
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

-- 3. PHÂN QUYỀN CHO VAI TRÒ (VAI_TRO_QUYEN)
-- Quản trị viên có tất cả quyền
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

-- 4. DỮ LIỆU NGƯỜI DÙNG (NGUOI_DUNG)
INSERT INTO nguoi_dung (ten_dang_nhap, mat_khau, ho_va_ten, email, so_dien_thoai, dia_chi, trang_thai_hoat_dong, tai_khoan_khong_bi_khoa) VALUES
-- Quản trị viên
('admin', '$2b$12$LQv3c1yqBw2rNZx8XQm5dOEzs4IrOQJGbqbw2Y3IpY6vR5qrO7Oje', 'Nguyễn Văn Admin', 'admin@truonghoc.edu.vn', '0901234567', 'Phòng IT, Tầng 1', true, true),

-- Lãnh đạo
('hieupho.nguyen', '$2b$12$LQv3c1yqBw2rNZx8XQm5dOEzs4IrOQJGbqbw2Y3IpY6vR5qrO7Oje', 'Nguyễn Văn Hiệu', 'hieupho@truonghoc.edu.vn', '0901234568', 'Phòng Hiệu trưởng', true, true),
('phong.tran', '$2b$12$LQv3c1yqBw2rNZx8XQm5dOEzs4IrOQJGbqbw2Y3IpY6vR5qrO7Oje', 'Trần Thị Phòng', 'phongcsvc@truonghoc.edu.vn', '0901234569', 'Phòng CSVC', true, true),

-- Nhân viên CSVC
('duc.le', '$2b$12$LQv3c1yqBw2rNZx8XQm5dOEzs4IrOQJGbqbw2Y3IpY6vR5qrO7Oje', 'Lê Văn Đức', 'duc.csvc@truonghoc.edu.vn', '0901234570', 'Phòng CSVC', true, true),
('mai.pham', '$2b$12$LQv3c1yqBw2rNZx8XQm5dOEzs4IrOQJGbqbw2Y3IpY6vR5qrO7Oje', 'Phạm Thị Mai', 'mai.csvc@truonghoc.edu.vn', '0901234571', 'Phòng CSVC', true, true),

-- Kỹ thuật viên
('thanh.vo', '$2b$12$LQv3c1yqBw2rNZx8XQm5dOEzs4IrOQJGbqbw2Y3IpY6vR5qrO7Oje', 'Võ Minh Thành', 'thanh.kt@truonghoc.edu.vn', '0901234572', 'Phòng Kỹ thuật', true, true),
('hung.dao', '$2b$12$LQv3c1yqBw2rNZx8XQm5dOEzs4IrOQJGbqbw2Y3IpY6vR5qrO7Oje', 'Đào Công Hùng', 'hung.kt@truonghoc.edu.vn', '0901234573', 'Phòng Kỹ thuật', true, true),

-- Giáo viên
('linh.nguyen', '$2b$12$LQv3c1yqBw2rNZx8XQm5dOEzs4IrOQJGbqbw2Y3IpY6vR5qrO7Oje', 'Nguyễn Thị Linh', 'linh.gv@truonghoc.edu.vn', '0901234574', 'Tổ Toán - Lý', true, true),
('minh.tran', '$2b$12$LQv3c1yqBw2rNZx8XQm5dOEzs4IrOQJGbqbw2Y3IpY6vR5qrO7Oje', 'Trần Văn Minh', 'minh.gv@truonghoc.edu.vn', '0901234575', 'Tổ Hóa - Sinh', true, true),
('hoa.le', '$2b$12$LQv3c1yqBw2rNZx8XQm5dOEzs4IrOQJGbqbw2Y3IpY6vR5qrO7Oje', 'Lê Thị Hoa', 'hoa.gv@truonghoc.edu.vn', '0901234576', 'Tổ Tin học', true, true);

-- 5. PHÂN VAI TRÒ CHO NGƯỜI DÙNG (NGUOI_DUNG_VAI_TRO)
INSERT INTO nguoi_dung_vai_tro (id_nguoi_dung, id_vai_tro)
SELECT nd.id_nguoi_dung, vt.id_vai_tro
FROM nguoi_dung nd, vai_tro vt
WHERE nd.ten_dang_nhap = 'admin' AND vt.ten_vai_tro = 'QUAN_TRI_VIEN';

INSERT INTO nguoi_dung_vai_tro (id_nguoi_dung, id_vai_tro)
SELECT nd.id_nguoi_dung, vt.id_vai_tro
FROM nguoi_dung nd, vai_tro vt
WHERE nd.ten_dang_nhap = 'hieupho.nguyen' AND vt.ten_vai_tro = 'HIEU_TRUONG';

INSERT INTO nguoi_dung_vai_tro (id_nguoi_dung, id_vai_tro)
SELECT nd.id_nguoi_dung, vt.id_vai_tro
FROM nguoi_dung nd, vai_tro vt
WHERE nd.ten_dang_nhap = 'phong.tran' AND vt.ten_vai_tro = 'TRUONG_PHONG_CSVC';

INSERT INTO nguoi_dung_vai_tro (id_nguoi_dung, id_vai_tro)
SELECT nd.id_nguoi_dung, vt.id_vai_tro
FROM nguoi_dung nd, vai_tro vt
WHERE nd.ten_dang_nhap IN ('duc.le', 'mai.pham') AND vt.ten_vai_tro = 'NHAN_VIEN_CSVC';

INSERT INTO nguoi_dung_vai_tro (id_nguoi_dung, id_vai_tro)
SELECT nd.id_nguoi_dung, vt.id_vai_tro
FROM nguoi_dung nd, vai_tro vt
WHERE nd.ten_dang_nhap IN ('thanh.vo', 'hung.dao') AND vt.ten_vai_tro = 'KY_THUAT_VIEN';

INSERT INTO nguoi_dung_vai_tro (id_nguoi_dung, id_vai_tro)
SELECT nd.id_nguoi_dung, vt.id_vai_tro
FROM nguoi_dung nd, vai_tro vt
WHERE nd.ten_dang_nhap IN ('linh.nguyen', 'minh.tran', 'hoa.le') AND vt.ten_vai_tro = 'GIAO_VIEN';

-- 6. TRẠNG THÁI THIẾT BỊ (TRANG_THAI_THIET_BI)
INSERT INTO trang_thai_thiet_bi (ma_trang_thai, ten_trang_thai, mo_ta, mau_sac, bieu_tuong, cho_phep_hoat_dong, yeu_cau_bao_tri, trang_thai_hoat_dong) VALUES
                                                                                                                                                           ('HOAT_DONG', 'Hoạt động bình thường', 'Thiết bị đang hoạt động tốt', 'green', 'bi-check-circle-fill', true, false, true),
                                                                                                                                                           ('BAO_TRI', 'Đang bảo trì', 'Thiết bị đang được bảo trì', 'yellow', 'bi-tools', false, true, true),
                                                                                                                                                           ('SUA_CHUA', 'Đang sửa chữa', 'Thiết bị đang được sửa chữa', 'orange', 'bi-wrench', false, true, true),
                                                                                                                                                           ('HONG', 'Hỏng', 'Thiết bị bị hỏng, cần sửa chữa', 'red', 'bi-exclamation-triangle-fill', false, true, true),
                                                                                                                                                           ('NGUNG_SU_DUNG', 'Ngừng sử dụng', 'Thiết bị tạm ngừng sử dụng', 'gray', 'bi-x-circle-fill', false, false, true),
                                                                                                                                                           ('CHO_THANH_LY', 'Chờ thanh lý', 'Thiết bị chờ thanh lý', 'black', 'bi-trash-fill', false, false, true);

-- 7. NHÓM THIẾT BỊ (NHOM_THIET_BI)
INSERT INTO nhom_thiet_bi (ma_nhom, ten_nhom, mo_ta, cap_do, thu_tu_hien_thi, trang_thai_hoat_dong) VALUES
-- Nhóm cấp 1
('PHONG_HOC', 'Thiết bị phòng học', 'Các thiết bị phục vụ giảng dạy trong phòng học', 1, 1, true),
('PHONG_LAB', 'Thiết bị phòng thí nghiệm', 'Thiết bị thí nghiệm khoa học', 1, 2, true),
('THIET_BI_IT', 'Thiết bị công nghệ thông tin', 'Máy tính, mạng, phần mềm', 1, 3, true),
('THIET_BI_VAN_PHONG', 'Thiết bị văn phòng', 'Máy in, máy photocopy, máy fax', 1, 4, true),
('HE_THONG_DIEN', 'Hệ thống điện', 'Hệ thống điện, máy phát điện', 1, 5, true),
('HE_THONG_NUOC', 'Hệ thống nước', 'Hệ thống cấp thoát nước', 1, 6, true),
('AN_NINH_AN_TOAN', 'An ninh - An toàn', 'Camera, báo cháy, an ninh', 1, 7, true);

-- Nhóm cấp 2 - Thiết bị phòng học
INSERT INTO nhom_thiet_bi (ma_nhom, ten_nhom, mo_ta, cap_do, thu_tu_hien_thi, id_nhom_cha, trang_thai_hoat_dong) VALUES
                                                                                                                     ('MAY_CHIEU', 'Máy chiếu', 'Máy chiếu đa phương tiện', 2, 1, (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'PHONG_HOC'), true),
                                                                                                                     ('BANG_DIEN_TU', 'Bảng điện tử', 'Bảng điện tử tương tác', 2, 2, (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'PHONG_HOC'), true),
                                                                                                                     ('LOA_MICRO', 'Loa - Micro', 'Hệ thống âm thanh', 2, 3, (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'PHONG_HOC'), true),
                                                                                                                     ('BAN_GHE', 'Bàn ghế', 'Bàn ghế học sinh, giáo viên', 2, 4, (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'PHONG_HOC'), true);

-- Nhóm cấp 2 - Thiết bị IT
INSERT INTO nhom_thiet_bi (ma_nhom, ten_nhom, mo_ta, cap_do, thu_tu_hien_thi, id_nhom_cha, trang_thai_hoat_dong) VALUES
                                                                                                                     ('MAY_TINH', 'Máy tính', 'Máy tính để bàn và laptop', 2, 1, (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'THIET_BI_IT'), true),
                                                                                                                     ('MANG_INTERNET', 'Mạng Internet', 'Router, Switch, Access Point', 2, 2, (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'THIET_BI_IT'), true),
                                                                                                                     ('SERVER', 'Máy chủ', 'Máy chủ và thiết bị lưu trữ', 2, 3, (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'THIET_BI_IT'), true);

-- 8. THIẾT BỊ (THIET_BI)
INSERT INTO thiet_bi (ma_thiet_bi, ten_thiet_bi, mo_ta, hang_san_xuat, model, so_serial, nam_san_xuat, ngay_lap_dat, ngay_dua_vao_su_dung, gia_tri_ban_dau, gia_tri_hien_tai, vi_tri_lap_dat, chu_ky_bao_tri_dinh_ky, trang_thai_hoat_dong, id_nhom_thiet_bi, id_trang_thai) VALUES

-- Máy chiếu
('MC001', 'Máy chiếu Epson EB-X41', 'Máy chiếu độ phân giải XGA', 'Epson', 'EB-X41', 'EP2021001', 2021, '2021-08-15', '2021-08-20', 15000000, 12000000, 'Phòng 101 - Tầng 1', 180, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_CHIEU'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

('MC002', 'Máy chiếu BenQ MX611', 'Máy chiếu độ sáng cao 4000 lumens', 'BenQ', 'MX611', 'BQ2021002', 2021, '2021-09-10', '2021-09-15', 18000000, 14000000, 'Phòng 102 - Tầng 1', 180, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_CHIEU'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

('MC003', 'Máy chiếu Panasonic PT-LB423', 'Máy chiếu LCD 4100 lumens', 'Panasonic', 'PT-LB423', 'PN2020001', 2020, '2020-07-20', '2020-08-01', 20000000, 15000000, 'Hội trường', 180, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_CHIEU'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'BAO_TRI')),

-- Bảng điện tử
('BDT001', 'Bảng tương tác IQBoard 82 inch', 'Bảng điện tử tương tác đa điểm', 'IQBoard', 'DVT TN082', 'IQ2022001', 2022, '2022-03-15', '2022-03-20', 45000000, 40000000, 'Phòng 201 - Tầng 2', 365, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'BANG_DIEN_TU'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

('BDT002', 'Bảng tương tác Smart Board 75 inch', 'Bảng thông minh cảm ứng', 'Smart Technologies', 'SMART-75', 'ST2022001', 2022, '2022-04-10', '2022-04-15', 50000000, 45000000, 'Phòng 202 - Tầng 2', 365, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'BANG_DIEN_TU'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

-- Máy tính
('MT001', 'Máy tính Dell OptiPlex 3080', 'PC để bàn Intel Core i5', 'Dell', 'OptiPlex 3080', 'DL2023001', 2023, '2023-01-15', '2023-01-20', 18000000, 16000000, 'Phòng tin học 1', 730, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_TINH'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

('MT002', 'Máy tính HP EliteDesk 800 G9', 'PC để bàn Intel Core i7', 'HP', 'EliteDesk 800 G9', 'HP2023001', 2023, '2023-02-10', '2023-02-15', 22000000, 20000000, 'Phòng tin học 2', 730, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_TINH'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

('LT001', 'Laptop Asus VivoBook 15', 'Laptop Intel Core i5, RAM 8GB', 'Asus', 'X515EA', 'AS2023001', 2023, '2023-03-05', '2023-03-10', 15000000, 13000000, 'Phòng giáo viên', 365, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_TINH'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

-- Thiết bị mạng
('RT001', 'Router Cisco ISR 4321', 'Router doanh nghiệp', 'Cisco', 'ISR4321', 'CS2022001', 2022, '2022-06-15', '2022-06-20', 25000000, 22000000, 'Phòng IT - Tầng 1', 365, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MANG_INTERNET'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

('SW001', 'Switch TP-Link TL-SG1024D', 'Switch 24 port Gigabit', 'TP-Link', 'TL-SG1024D', 'TP2022001', 2022, '2022-07-10', '2022-07-15', 3500000, 3000000, 'Tủ rack tầng 1', 730, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MANG_INTERNET'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

('AP001', 'Access Point Ubiquiti UniFi', 'WiFi Access Point', 'Ubiquiti', 'UAP-AC-LR', 'UB2022001', 2022, '2022-08-05', '2022-08-10', 4000000, 3500000, 'Hành lang tầng 1', 365, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MANG_INTERNET'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

-- Hệ thống âm thanh
('LOA001', 'Loa JBL EON612', 'Loa sân khấu chuyên nghiệp', 'JBL', 'EON612', 'JB2021001', 2021, '2021-05-15', '2021-05-20', 12000000, 10000000, 'Hội trường', 180, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'LOA_MICRO'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

('MIC001', 'Micro không dây Shure SM58', 'Micro cầm tay không dây', 'Shure', 'SM58-LC', 'SH2021001', 2021, '2021-06-10', '2021-06-15', 8000000, 7000000, 'Hội trường', 365, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'LOA_MICRO'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

-- Máy in
('IN001', 'Máy in HP LaserJet Pro M404dn', 'Máy in laser đen trắng', 'HP', 'M404dn', 'HP2023002', 2023, '2023-01-20', '2023-01-25', 8500000, 7500000, 'Phòng văn phòng', 180, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'THIET_BI_VAN_PHONG'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

('IN002', 'Máy photocopy Canon iR2425', 'Máy photocopy đa chức năng', 'Canon', 'iR2425', 'CN2022002', 2022, '2022-09-15', '2022-09-20', 35000000, 30000000, 'Phòng in ấn', 90, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'THIET_BI_VAN_PHONG'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'SUA_CHUA')),

-- Điều hòa
('DH001', 'Điều hòa Daikin FTKC35UAVMV', 'Điều hòa inverter 1.5HP', 'Daikin', 'FTKC35UAVMV', 'DK2022001', 2022, '2022-04-20', '2022-05-01', 18000000, 16000000, 'Phòng 101', 90, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'HE_THONG_DIEN'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

('DH002', 'Điều hòa Mitsubishi MSZ-HL35VA', 'Điều hòa 1.5HP tiết kiệm điện', 'Mitsubishi', 'MSZ-HL35VA', 'MT2022001', 2022, '2022-05-15', '2022-05-25', 16500000, 15000000, 'Phòng 102', 90, true,
 (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'HE_THONG_DIEN'),
 (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'BAO_TRI'));

-- 9. ĐỘI BẢO TRÌ (DOI_BAO_TRI)
INSERT INTO doi_bao_tri (ma_doi, ten_doi, mo_ta, chuyen_mon, khu_vuc_hoat_dong, ca_lam_viec, so_thanh_vien_toi_da, so_thanh_vien_hien_tai, trang_thai_hoat_dong, ngay_thanh_lap, id_truong_doi) VALUES
                                                                                                                                                                                                    ('DBT001', 'Đội Bảo trì Thiết bị IT', 'Chuyên bảo trì thiết bị công nghệ thông tin', 'DIEN_TU', 'Toàn trường', 'CA_HANH_CHINH', 5, 2, true, '2023-01-01',
                                                                                                                                                                                                     (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

                                                                                                                                                                                                    ('DBT002', 'Đội Bảo trì Cơ điện', 'Chuyên bảo trì hệ thống điện, điều hòa', 'DIEN', 'Toàn trường', 'CA_HANH_CHINH', 4, 1, true, '2023-01-01',
                                                                                                                                                                                                     (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao')),

                                                                                                                                                                                                    ('DBT003', 'Đội Bảo trì Tổng hợp', 'Bảo trì các thiết bị khác', 'TONG_HOP', 'Toàn trường', 'CA_HANH_CHINH', 6, 0, true, '2023-01-01',
                                                                                                                                                                                                     (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'duc.le'));

-- 10. THÀNH VIÊN ĐỘI (THANH_VIEN_DOI)
INSERT INTO thanh_vien_doi (chuc_vu, ngay_tham_gia, trang_thai_hoat_dong, muc_do_kinh_nghiem, ky_nang_chuyen_mon, id_doi_bao_tri, id_nguoi_dung) VALUES
-- Đội IT
('TRUONG_DOI', '2023-01-01', true, 4, 'Bảo trì máy tính, mạng, phần mềm',
 (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

-- Đội Cơ điện  
('TRUONG_DOI', '2023-01-01', true, 5, 'Bảo trì hệ thống điện, điều hòa, máy phát điện',
 (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT002'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao')),

-- Đội Tổng hợp
('TRUONG_DOI', '2023-01-01', true, 3, 'Bảo trì thiết bị văn phòng, đồ dùng học tập',
 (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT003'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'duc.le'));

-- 11. KẾ HOẠCH BẢO TRÌ (KE_HOACH_BAO_TRI)
INSERT INTO ke_hoach_bao_tri (ma_ke_hoach, ten_ke_hoach, mo_ta, loai_ke_hoach, ngay_bat_dau, ngay_ket_thuc, chu_ky_lap_lai, trang_thai, ty_le_hoan_thanh, id_nguoi_tao) VALUES
                                                                                                                                                                            ('KH2024Q1', 'Kế hoạch bảo trì Quý 1/2024', 'Bảo trì định kỳ các thiết bị trong quý 1', 'HANG_QUY', '2024-01-01 00:00:00', '2024-03-31 23:59:59', 90, 'DANG_THUC_HIEN', 65,
                                                                                                                                                                             (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'phong.tran')),

                                                                                                                                                                            ('KH2024Q2', 'Kế hoạch bảo trì Quý 2/2024', 'Bảo trì định kỳ các thiết bị trong quý 2', 'HANG_QUY', '2024-04-01 00:00:00', '2024-06-30 23:59:59', 90, 'DUYET', 0,
                                                                                                                                                                             (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'phong.tran')),

                                                                                                                                                                            ('KHBT_DHOA_2024', 'Kế hoạch bảo trì điều hòa 2024', 'Vệ sinh, bảo dưỡng điều hòa trước mùa nóng', 'HANG_NAM', '2024-03-01 00:00:00', '2024-04-30 23:59:59', 365, 'HOAN_THANH', 100,
                                                                                                                                                                             (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao'));

-- 12. YÊU CẦU BẢO TRÌ (YEU_CAU_BAO_TRI)
INSERT INTO yeu_cau_bao_tri (ma_yeu_cau, tieu_de, mo_ta, loai_yeu_cau, muc_do_uu_tien, trang_thai, ngay_yeu_cau, ngay_mong_muon, thoi_gian_du_kien, chi_phi_du_kien, yeu_cau_dung_may, co_anh_huong_san_xuat, can_vat_tu_dac_biet, ghi_chu, id_thiet_bi, id_nguoi_yeu_cau, id_ke_hoach) VALUES

                                                                                                                                                                                                                                                                                            ('YC2024001', 'Thay bóng đèn máy chiếu Panasonic', 'Bóng đèn máy chiếu đã mờ, cần thay thế', 'THAY_THE_LINH_KIEN', 3, 'CHO_DUYET', '2024-01-15 08:30:00', '2024-01-25 17:00:00', 120, 3500000, false, true, true, 'Ưu tiên thay để phục vụ giảng dạy',
                                                                                                                                                                                                                                                                                             (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MC003'),
                                                                                                                                                                                                                                                                                             (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'linh.nguyen'),
                                                                                                                                                                                                                                                                                             (SELECT id_ke_hoach FROM ke_hoach_bao_tri WHERE ma_ke_hoach = 'KH2024Q1')),

                                                                                                                                                                                                                                                                                            ('YC2024002', 'Sửa chữa máy photocopy Canon', 'Máy bị kẹt giấy thường xuyên, cần kiểm tra và sửa chữa', 'SUA_CHUA_KHAN_CAP', 4, 'DANG_THUC_HIEN', '2024-01-20 09:15:00', '2024-01-22 17:00:00', 240, 5000000, true, false, true, 'Máy dùng chung của trường, cần sửa gấp',
                                                                                                                                                                                                                                                                                             (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'IN002'),
                                                                                                                                                                                                                                                                                             (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'mai.pham'),
                                                                                                                                                                                                                                                                                             NULL),

                                                                                                                                                                                                                                                                                            ('YC2024003', 'Vệ sinh điều hòa phòng 102', 'Điều hòa bám bụi, cần vệ sinh filter và dàn lạnh', 'BAO_TRI_DINH_KY', 2, 'HOAN_THANH', '2024-02-01 14:00:00', '2024-02-10 17:00:00', 180, 800000, false, false, false, 'Bảo trì định kỳ trước mùa nóng',
                                                                                                                                                                                                                                                                                             (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'DH002'),
                                                                                                                                                                                                                                                                                             (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'duc.le'),
                                                                                                                                                                                                                                                                                             (SELECT id_ke_hoach FROM ke_hoach_bao_tri WHERE ma_ke_hoach = 'KHBT_DHOA_2024')),

                                                                                                                                                                                                                                                                                            ('YC2024004', 'Kiểm tra hệ thống mạng', 'Internet thường xuyên bị chậm, cần kiểm tra router và switch', 'KIEM_TRA_AN_TOAN', 3, 'DA_DUYET', '2024-02-05 10:20:00', '2024-02-08 17:00:00', 300, 0, false, true, false, 'Ảnh hưởng đến việc dạy học online',
                                                                                                                                                                                                                                                                                             (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'RT001'),
                                                                                                                                                                                                                                                                                             (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hoa.le'),
                                                                                                                                                                                                                                                                                             NULL),

                                                                                                                                                                                                                                                                                            ('YC2024005', 'Cập nhật phần mềm máy tính', 'Cần cập nhật Windows và phần mềm office cho máy tính phòng tin học', 'NAY_CAP_THIET_BI', 2, 'CHO_DUYET', '2024-02-10 08:00:00', '2024-02-20 17:00:00', 480, 2000000, true, true, false, 'Cập nhật để bảo mật và hiệu suất tốt hơn',
                                                                                                                                                                                                                                                                                             (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MT001'),
                                                                                                                                                                                                                                                                                             (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'minh.tran'),
                                                                                                                                                                                                                                                                                             (SELECT id_ke_hoach FROM ke_hoach_bao_tri WHERE ma_ke_hoach = 'KH2024Q1'));

-- 13. NHÓM VẬT TƯ (NHOM_VAT_TU)
INSERT INTO nhom_vat_tu (ma_nhom, ten_nhom, mo_ta, cap_do, thu_tu_hien_thi, trang_thai_hoat_dong) VALUES
                                                                                                      ('PHU_TUNG_IT', 'Phụ tùng IT', 'Linh kiện máy tính, mạng', 1, 1, true),
                                                                                                      ('VAT_TU_DIEN', 'Vật tư điện', 'Dây điện, ổ cắm, cầu dao', 1, 2, true),
                                                                                                      ('HOA_CHAT_VS', 'Hóa chất vệ sinh', 'Chất tẩy rửa, vệ sinh thiết bị', 1, 3, true),
                                                                                                      ('PHU_TUNG_DHOA', 'Phụ tùng điều hòa', 'Linh kiện thay thế cho điều hòa', 1, 4, true),
                                                                                                      ('DUNG_CU_BT', 'Dụng cụ bảo trì', 'Công cụ, dụng cụ sửa chữa', 1, 5, true);

-- 14. VẬT TƯ (VAT_TU)
INSERT INTO vat_tu (ma_vat_tu, ten_vat_tu, mo_ta, don_vi_tinh, gia_nhap, gia_xuat, so_luong_ton_kho, so_luong_ton_toi_thieu, so_luong_ton_toi_da, muc_do_quan_trong, loai_vat_tu, hang_san_xuat, model, co_the_thay_the, vat_tu_quan_trong, trang_thai_hoat_dong, id_nhom_vat_tu) VALUES

-- Phụ tùng IT
('RAM8GB', 'RAM DDR4 8GB', 'Bộ nhớ RAM DDR4 8GB PC2400', 'Cái', 1200000, 1350000, 10, 3, 20, 3, 'PHU_TUNG', 'Kingston', 'KVR24N17S8/8', true, true, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'PHU_TUNG_IT')),

('SSD256GB', 'SSD 256GB', 'Ổ cứng SSD SATA 256GB', 'Cái', 1500000, 1650000, 8, 2, 15, 3, 'PHU_TUNG', 'Samsung', '860 EVO', true, true, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'PHU_TUNG_IT')),

('CABLE_LAN', 'Dây cáp mạng Cat6', 'Cáp mạng Cat6 UTP 305m', 'Cuộn', 2500000, 2800000, 3, 1, 10, 2, 'VAT_TU_CHUNG', 'AMP', 'Cat6 UTP', false, false, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'PHU_TUNG_IT')),

-- Vật tư điện
('DAY_DIEN_2x2.5', 'Dây điện đôi 2x2.5', 'Dây điện đôi CVV-U 2x2.5mm2', 'Mét', 25000, 28000, 150, 50, 500, 2, 'VAT_TU_CHUNG', 'Cadivi', 'CVV-U 2x2.5', false, false, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'VAT_TU_DIEN')),

('O_CAM_3_LO', 'Ổ cắm 3 lỗ', 'Ổ cắm điện 3 lỗ có nắp đậy', 'Cái', 45000, 50000, 25, 10, 50, 2, 'VAT_TU_CHUNG', 'Panasonic', 'WEG1112', true, false, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'VAT_TU_DIEN')),

('CAU_DAO_15A', 'Cầu dao 15A', 'Cầu dao tự động 15A 1 pha', 'Cái', 180000, 200000, 12, 5, 30, 3, 'PHU_TUNG', 'Schneider', 'C60N-C15', false, true, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'VAT_TU_DIEN')),

-- Hóa chất vệ sinh
('CHAT_TAY_BUI', 'Chất tẩy bụi điện tử', 'Chai xịt tẩy bụi cho thiết bị điện tử', 'Chai', 120000, 135000, 20, 5, 40, 1, 'HOA_CHAT', 'CRC', 'QD Electronic Cleaner', false, false, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'HOA_CHAT_VS')),

('KHAN_VE_SINH', 'Khăn vệ sinh vi sợi', 'Khăn lau thiết bị điện tử chống tĩnh điện', 'Cái', 35000, 40000, 30, 10, 60, 1, 'VAT_TU_TIEU_HAO', 'Cleanroom', 'Microfiber', false, false, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'HOA_CHAT_VS')),

-- Phụ tùng điều hòa
('FILTER_DHOA', 'Lưới lọc điều hòa', 'Lưới lọc cho điều hòa 1.5HP', 'Cái', 150000, 170000, 15, 5, 30, 2, 'PHU_TUNG', 'Daikin', 'FTKC35', true, false, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'PHU_TUNG_DHOA')),

('REMOTE_DHOA', 'Remote điều hòa', 'Remote điều khiển điều hòa Daikin', 'Cái', 350000, 400000, 8, 3, 15, 2, 'PHU_TUNG', 'Daikin', 'ARC480A54', true, false, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'PHU_TUNG_DHOA')),

-- Dụng cụ bảo trì
('TKEY_NHIEU_SIZE', 'Bộ tuốc nơ vít đa năng', 'Bộ tuốc nơ vít từ tính nhiều size', 'Bộ', 450000, 500000, 5, 2, 10, 2, 'CONG_CU', 'Stanley', 'STMT71653', false, false, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'DUNG_CU_BT')),

('DONG_HO_VAN_NANG', 'Đồng hồ vạn năng', 'Đồng hồ đo điện đa năng', 'Cái', 850000, 950000, 3, 1, 5, 3, 'THIET_BI_DO_LUONG', 'Fluke', '117', false, true, true,
 (SELECT id_nhom_vat_tu FROM nhom_vat_tu WHERE ma_nhom = 'DUNG_CU_BT'));

-- 15. KHO VẬT TƯ (KHO_VAT_TU)
INSERT INTO kho_vat_tu (ma_kho, ten_kho, mo_ta, vi_tri, so_luong_ton, so_luong_kha_dung, gia_tri_ton_kho, kho_chinh, trang_thai_hoat_dong, id_vat_tu, id_thu_kho) VALUES

-- Kho cho các vật tư
('KHO_IT_001', 'Kho phụ tùng IT - RAM 8GB', 'Kho lưu trữ RAM DDR4 8GB', 'Phòng kho tầng 1', 10, 10, 13500000, true, true,
 (SELECT id_vat_tu FROM vat_tu WHERE ma_vat_tu = 'RAM8GB'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'duc.le')),

('KHO_IT_002', 'Kho phụ tùng IT - SSD 256GB', 'Kho lưu trữ SSD 256GB', 'Phòng kho tầng 1', 8, 8, 13200000, true, true,
 (SELECT id_vat_tu FROM vat_tu WHERE ma_vat_tu = 'SSD256GB'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'duc.le')),

('KHO_DIEN_001', 'Kho vật tư điện - Dây điện', 'Kho lưu trữ dây điện các loại', 'Phòng kho tầng 1', 150, 150, 4200000, true, true,
 (SELECT id_vat_tu FROM vat_tu WHERE ma_vat_tu = 'DAY_DIEN_2x2.5'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'duc.le')),

('KHO_VS_001', 'Kho hóa chất vệ sinh', 'Kho lưu trữ chất tẩy rửa', 'Phòng kho tầng 1', 20, 20, 2700000, true, true,
 (SELECT id_vat_tu FROM vat_tu WHERE ma_vat_tu = 'CHAT_TAY_BUI'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'mai.pham'));

-- 16. MẪU PHIẾU KIỂM TRA (MAU_PHIEU_KIEM_TRA)
INSERT INTO mau_phieu_kiem_tra (ma_mau_phieu, ten_mau_phieu, mo_ta, loai_thiet_bi, loai_kiem_tra, trang_thai_hoat_dong, id_nguoi_tao) VALUES
                                                                                                                                          ('MPK_MAYCHIEU', 'Mẫu phiếu kiểm tra máy chiếu', 'Phiếu kiểm tra định kỳ máy chiếu', 'MAY_CHIEU', 'KIEM_TRA_KY_THUAT', true,
                                                                                                                                           (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

                                                                                                                                          ('MPK_MAYTINH', 'Mẫu phiếu kiểm tra máy tính', 'Phiếu kiểm tra định kỳ máy tính', 'MAY_TINH', 'KIEM_TRA_KY_THUAT', true,
                                                                                                                                           (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

                                                                                                                                          ('MPK_DIEUHOA', 'Mẫu phiếu kiểm tra điều hòa', 'Phiếu kiểm tra định kỳ điều hòa', 'DIEU_HOA', 'KIEM_TRA_KY_THUAT', true,
                                                                                                                                           (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao'));

-- 17. HẠNG MỤC KIỂM TRA (HANG_MUC_KIEM_TRA)
INSERT INTO hang_muc_kiem_tra (ten_hang_muc, mo_ta, tieu_chi, thu_tu, bat_buoc, loai_kiem_tra, diem_toi_da, ghi_chu, id_mau_phieu) VALUES

-- Hạng mục cho máy chiếu
('Kiểm tra nguồn điện', 'Kiểm tra dây nguồn và khả năng cấp điện', 'Dây nguồn không bị hỏng, cắm điện ổn định', 1, true, 'AN_TOAN', 10, 'Ưu tiên kiểm tra trước',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_MAYCHIEU')),

('Kiểm tra độ sáng', 'Đo độ sáng của máy chiếu', 'Độ sáng đạt tối thiểu 80% so với thông số kỹ thuật', 2, true, 'KY_THUAT', 20, 'Sử dụng máy đo lux',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_MAYCHIEU')),

('Kiểm tra chất lượng hình ảnh', 'Kiểm tra độ nét, màu sắc', 'Hình ảnh rõ nét, màu sắc chân thực', 3, true, 'CHAT_LUONG', 30, 'Chiếu thử slide test',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_MAYCHIEU')),

('Vệ sinh máy chiếu', 'Làm sạch bụi bẩn, kiểm tra filter', 'Máy sạch sẽ, filter không bị tắc', 4, false, 'BAO_DUONG', 20, 'Vệ sinh 3 tháng/lần',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_MAYCHIEU')),

('Kiểm tra remote', 'Kiểm tra hoạt động của remote điều khiển', 'Remote hoạt động bình thường ở khoảng cách 5m', 5, false, 'CHUC_NANG', 20, 'Thay pin nếu cần',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_MAYCHIEU')),

-- Hạng mục cho máy tính
('Kiểm tra khởi động', 'Kiểm tra thời gian và quá trình khởi động', 'Khởi động dưới 60 giây, không lỗi', 1, true, 'KY_THUAT', 15, 'Đo thời gian chính xác',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_MAYTINH')),

('Kiểm tra phần cứng', 'Kiểm tra RAM, CPU, ổ cứng', 'Tất cả phần cứng hoạt động bình thường', 2, true, 'KY_THUAT', 25, 'Sử dụng phần mềm test',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_MAYTINH')),

('Kiểm tra phần mềm', 'Kiểm tra hệ điều hành và ứng dụng', 'Windows và Office hoạt động ổn định', 3, true, 'CHUC_NANG', 20, 'Cập nhật nếu cần',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_MAYTINH')),

('Kiểm tra kết nối mạng', 'Test kết nối Internet và mạng nội bộ', 'Tốc độ mạng đạt yêu cầu', 4, true, 'CHUC_NANG', 20, 'Kiểm tra cáp mạng',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_MAYTINH')),

('Vệ sinh máy tính', 'Làm sạch bụi bẩn bên trong và ngoài', 'Máy sạch sẽ, tản nhiệt tốt', 5, false, 'BAO_DUONG', 20, 'Vệ sinh 6 tháng/lần',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_MAYTINH')),

-- Hạng mục cho điều hòa
('Kiểm tra nguồn điện', 'Kiểm tra dây nguồn và aptomat', 'Nguồn điện ổn định, aptomat không trip', 1, true, 'AN_TOAN', 10, 'Kiểm tra điện áp',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_DIEUHOA')),

('Kiểm tra gas làm lạnh', 'Đo áp suất gas và hiệu quả làm lạnh', 'Áp suất gas trong phạm vi cho phép', 2, true, 'KY_THUAT', 25, 'Dùng đồng hồ đo áp suất',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_DIEUHOA')),

('Vệ sinh dàn lạnh', 'Làm sạch dàn lạnh và filter', 'Dàn lạnh sạch, không có nấm mốc', 3, true, 'BAO_DUONG', 20, 'Sử dụng hóa chất chuyên dụng',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_DIEUHOA')),

('Vệ sinh dàn nóng', 'Làm sạch dàn nóng ngoài trời', 'Dàn nóng sạch, tản nhiệt tốt', 4, true, 'BAO_DUONG', 20, 'Rửa bằng nước áp lực cao',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_DIEUHOA')),

('Kiểm tra remote và cảm biến', 'Test remote và cảm biến nhiệt độ', 'Remote hoạt động, cảm biến chính xác', 5, false, 'CHUC_NANG', 15, 'Kiểm tra pin remote',
 (SELECT id_mau_phieu FROM mau_phieu_kiem_tra WHERE ma_mau_phieu = 'MPK_DIEUHOA'));

-- 18. KIỂM TRA ĐỊNH KỲ (KIEM_TRA_DINH_KY)
INSERT INTO kiem_tra_dinh_ky (ma_kiem_tra, ten_kiem_tra, mo_ta, loai_kiem_tra, chu_ky_kiem_tra, ngay_kiem_tra, ngay_kiem_tra_tiep_theo, trang_thai, ket_qua_kiem_tra, danh_gia_tong_the, kien_nghi, thoi_gian_thuc_hien, chi_phi, yeu_cau_bao_tri, id_thiet_bi, id_nguoi_kiem_tra) VALUES

                                                                                                                                                                                                                                                                                       ('KT2024001', 'Kiểm tra máy chiếu Epson EB-X41 - Q1/2024', 'Kiểm tra định kỳ quý 1', 'KIEM_TRA_KY_THUAT', 90, '2024-01-10 08:00:00', '2024-04-10 08:00:00', 'HOAN_THANH', 'Máy hoạt động bình thường, độ sáng đạt 85%', 'Tốt', 'Cần thay bóng đèn trong 6 tháng tới', 90, 0, false,
                                                                                                                                                                                                                                                                                        (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MC001'),
                                                                                                                                                                                                                                                                                        (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

                                                                                                                                                                                                                                                                                       ('KT2024002', 'Kiểm tra máy tính Dell OptiPlex - Q1/2024', 'Kiểm tra định kỳ quý 1', 'KIEM_TRA_KY_THUAT', 180, '2024-01-15 09:00:00', '2024-07-15 09:00:00', 'HOAN_THANH', 'Hệ thống hoạt động ổn định, cần cập nhật Windows', 'Tốt', 'Nâng cấp RAM lên 16GB để cải thiện hiệu suất', 120, 0, false,
                                                                                                                                                                                                                                                                                        (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MT001'),
                                                                                                                                                                                                                                                                                        (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

                                                                                                                                                                                                                                                                                       ('KT2024003', 'Kiểm tra điều hòa Daikin - Bảo dưỡng mùa nóng', 'Bảo dưỡng trước mùa nóng 2024', 'KIEM_TRA_KY_THUAT', 180, '2024-03-05 14:00:00', '2024-09-05 14:00:00', 'HOAN_THANH', 'Đã vệ sinh dàn lạnh, dàn nóng. Gas đầy đủ', 'Rất tốt', 'Máy hoạt động tốt, tiếp tục theo dõi', 150, 300000, false,
                                                                                                                                                                                                                                                                                        (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'DH001'),
                                                                                                                                                                                                                                                                                        (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao')),

                                                                                                                                                                                                                                                                                       ('KT2024004', 'Kiểm tra Router Cisco - An ninh mạng', 'Kiểm tra bảo mật và hiệu suất mạng', 'KIEM_TRA_AN_TOAN', 90, '2024-02-20 10:00:00', '2024-05-20 10:00:00', 'DANG_THUC_HIEN', '', '', '', NULL, NULL, false,
                                                                                                                                                                                                                                                                                        (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'RT001'),
                                                                                                                                                                                                                                                                                        (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo'));

-- 19. CHI TIẾT KIỂM TRA (CHI_TIET_KIEM_TRA)
INSERT INTO chi_tiet_kiem_tra (ten_hang_muc, tieu_chi, ket_qua, dat_chuan, diem_so, ghi_chu, id_kiem_tra) VALUES

-- Chi tiết kiểm tra máy chiếu MC001
('Kiểm tra nguồn điện', 'Dây nguồn không bị hỏng, cắm điện ổn định', 'Dây nguồn tốt, điện áp 220V ổn định', true, 10, 'Bình thường',
 (SELECT id_kiem_tra FROM kiem_tra_dinh_ky WHERE ma_kiem_tra = 'KT2024001')),

('Kiểm tra độ sáng', 'Độ sáng đạt tối thiểu 80% so với thông số kỹ thuật', 'Độ sáng đo được 2800 lumens (85% so với 3300 lumens)', true, 18, 'Còn tốt',
 (SELECT id_kiem_tra FROM kiem_tra_dinh_ky WHERE ma_kiem_tra = 'KT2024001')),

('Kiểm tra chất lượng hình ảnh', 'Hình ảnh rõ nét, màu sắc chân thực', 'Hình ảnh rõ nét, màu sắc tự nhiên', true, 30, 'Xuất sắc',
 (SELECT id_kiem_tra FROM kiem_tra_dinh_ky WHERE ma_kiem_tra = 'KT2024001')),

('Vệ sinh máy chiếu', 'Máy sạch sẽ, filter không bị tắc', 'Đã vệ sinh, filter sạch', true, 20, 'Đã vệ sinh',
 (SELECT id_kiem_tra FROM kiem_tra_dinh_ky WHERE ma_kiem_tra = 'KT2024001')),

-- Chi tiết kiểm tra máy tính MT001
('Kiểm tra khởi động', 'Khởi động dưới 60 giây, không lỗi', 'Khởi động trong 45 giây, không lỗi', true, 15, 'Tốt',
 (SELECT id_kiem_tra FROM kiem_tra_dinh_ky WHERE ma_kiem_tra = 'KT2024002')),

('Kiểm tra phần cứng', 'Tất cả phần cứng hoạt động bình thường', 'RAM 8GB, CPU i5 hoạt động ổn định', true, 25, 'Bình thường',
 (SELECT id_kiem_tra FROM kiem_tra_dinh_ky WHERE ma_kiem_tra = 'KT2024002')),

('Kiểm tra phần mềm', 'Windows và Office hoạt động ổn định', 'Windows 11, Office 2021 cần cập nhật', true, 18, 'Cần cập nhật',
 (SELECT id_kiem_tra FROM kiem_tra_dinh_ky WHERE ma_kiem_tra = 'KT2024002')),

-- Chi tiết kiểm tra điều hòa DH001
('Kiểm tra nguồn điện', 'Nguồn điện ổn định, aptomat không trip', 'Điện áp 220V ổn định, aptomat 25A tốt', true, 10, 'Bình thường',
 (SELECT id_kiem_tra FROM kiem_tra_dinh_ky WHERE ma_kiem_tra = 'KT2024003')),

('Kiểm tra gas làm lạnh', 'Áp suất gas trong phạm vi cho phép', 'Áp suất thấp: 0.5MPa, áp suất cao: 1.8MPa', true, 25, 'Gas đầy đủ',
 (SELECT id_kiem_tra FROM kiem_tra_dinh_ky WHERE ma_kiem_tra = 'KT2024003')),

('Vệ sinh dàn lạnh', 'Dàn lạnh sạch, không có nấm mốc', 'Đã vệ sinh hoàn toàn, không có nấm mốc', true, 20, 'Đã vệ sinh',
 (SELECT id_kiem_tra FROM kiem_tra_dinh_ky WHERE ma_kiem_tra = 'KT2024003'));

-- 20. LỊCH SỬ BẢO TRÌ (LICH_SU_BAO_TRI)
INSERT INTO lich_su_bao_tri (ngay_bao_tri, loai_bao_tri, noi_dung, ket_qua, thoi_gian_thuc_hien, chi_phi, vat_tu_su_dung, id_thiet_bi, id_nguoi_bao_tri) VALUES

                                                                                                                                                             ('2024-01-05 09:00:00', 'BAO_TRI_DINH_KY', 'Vệ sinh và kiểm tra máy chiếu Epson', 'Hoàn thành tốt, máy hoạt động bình thường', 90, 0, 'Chất tẩy bụi điện tử, khăn vi sợi',
                                                                                                                                                              (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MC001'),
                                                                                                                                                              (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

                                                                                                                                                             ('2024-01-20 14:00:00', 'SUA_CHUA_KHAN_CAP', 'Sửa chữa máy photocopy bị kẹt giấy', 'Đã thay roller và làm sạch đường giấy', 180, 2500000, 'Roller mới, dầu bôi trơn',
                                                                                                                                                              (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'IN002'),
                                                                                                                                                              (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao')),

                                                                                                                                                             ('2024-02-15 08:00:00', 'BAO_DUONG_PHONG_NGUA', 'Vệ sinh điều hòa phòng 102', 'Vệ sinh dàn lạnh, thay filter mới', 120, 800000, 'Filter mới, hóa chất vệ sinh',
                                                                                                                                                              (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'DH002'),
                                                                                                                                                              (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao')),

                                                                                                                                                             ('2024-03-01 10:00:00', 'THAY_THE_LINH_KIEN', 'Nâng cấp RAM cho máy tính phòng tin học', 'Nâng cấp thành công từ 8GB lên 16GB', 60, 1350000, 'RAM DDR4 8GB',
                                                                                                                                                              (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MT002'),
                                                                                                                                                              (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

                                                                                                                                                             ('2024-03-10 15:00:00', 'KIEM_TRA_AN_TOAN', 'Kiểm tra hệ thống mạng và bảo mật', 'Cập nhật firmware router, tăng cường bảo mật', 180, 0, 'Không',
                                                                                                                                                              (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'RT001'),
                                                                                                                                                              (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo'));

-- 21. LỊCH SỬ NHẬP XUẤT VẬT TƯ (LICH_SU_NHAP_XUAT)
INSERT INTO lich_su_nhap_xuat (loai_giao_dich, so_luong, don_gia, thanh_tien, so_luong_ton_truoc, so_luong_ton_sau, ngay_giao_dich, ly_do, ghi_chu, so_chung_tu, id_vat_tu, id_kho_vat_tu, id_nguoi_thuc_hien) VALUES

-- Nhập kho RAM
('NHAP', 15, 1200000, 18000000, 0, 15, '2023-12-15 08:00:00', 'Nhập kho đầu năm', 'Lô hàng mới từ nhà cung cấp', 'NK2023001',
 (SELECT id_vat_tu FROM vat_tu WHERE ma_vat_tu = 'RAM8GB'),
 (SELECT id_kho_vat_tu FROM kho_vat_tu WHERE ma_kho = 'KHO_IT_001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'duc.le')),

-- Xuất kho RAM để nâng cấp máy tính
('XUAT', 5, 1350000, 6750000, 15, 10, '2024-03-01 09:00:00', 'Nâng cấp máy tính phòng tin học', 'Nâng cấp theo kế hoạch', 'XK2024001',
 (SELECT id_vat_tu FROM vat_tu WHERE ma_vat_tu = 'RAM8GB'),
 (SELECT id_kho_vat_tu FROM kho_vat_tu WHERE ma_kho = 'KHO_IT_001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

-- Nhập kho SSD
('NHAP', 10, 1500000, 15000000, 0, 10, '2023-12-20 10:00:00', 'Nhập kho đầu năm', 'Chuẩn bị cho năm học mới', 'NK2023002',
 (SELECT id_vat_tu FROM vat_tu WHERE ma_vat_tu = 'SSD256GB'),
 (SELECT id_kho_vat_tu FROM kho_vat_tu WHERE ma_kho = 'KHO_IT_002'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'duc.le')),

-- Xuất kho SSD để thay thế
('XUAT', 2, 1650000, 3300000, 10, 8, '2024-02-10 14:00:00', 'Thay thế ổ cứng hỏng', 'Ổ cứng cũ bị bad sector', 'XK2024002',
 (SELECT id_vat_tu FROM vat_tu WHERE ma_vat_tu = 'SSD256GB'),
 (SELECT id_kho_vat_tu FROM kho_vat_tu WHERE ma_kho = 'KHO_IT_002'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

-- Nhập hóa chất vệ sinh
('NHAP', 30, 120000, 3600000, 0, 30, '2024-01-05 11:00:00', 'Bổ sung hóa chất vệ sinh', 'Chuẩn bị cho đợt vệ sinh định kỳ', 'NK2024001',
 (SELECT id_vat_tu FROM vat_tu WHERE ma_vat_tu = 'CHAT_TAY_BUI'),
 (SELECT id_kho_vat_tu FROM kho_vat_tu WHERE ma_kho = 'KHO_VS_001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'mai.pham')),

-- Xuất hóa chất để vệ sinh
('XUAT', 10, 135000, 1350000, 30, 20, '2024-01-10 08:00:00', 'Vệ sinh thiết bị định kỳ', 'Vệ sinh máy chiếu và máy tính', 'XK2024003',
 (SELECT id_vat_tu FROM vat_tu WHERE ma_vat_tu = 'CHAT_TAY_BUI'),
 (SELECT id_kho_vat_tu FROM kho_vat_tu WHERE ma_kho = 'KHO_VS_001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo'));

-- 22. CẢNH BÁO LỖI (CANH_BAO_LOI)
INSERT INTO canh_bao_loi (ma_canh_bao, tieu_de, mo_ta, loai_canh_bao, muc_do_nghiem_trong, trang_thai, ngay_phat_sinh, tu_dong_tao, can_thong_bao_ngay, da_gui_thong_bao, ghi_chu, id_thiet_bi, id_nguoi_xu_ly) VALUES

                                                                                                                                                                                                                    ('CB2024001', 'Máy chiếu Panasonic quá nóng', 'Máy chiếu hoạt động liên tục 4 giờ, nhiệt độ cao bất thường', 'NHIET_DO_CAO', 3, 'DA_XU_LY', '2024-01-12 14:30:00', true, true, true, 'Đã tắt máy để nguội và kiểm tra',
                                                                                                                                                                                                                     (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MC003'),
                                                                                                                                                                                                                     (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

                                                                                                                                                                                                                    ('CB2024002', 'Mạng Internet chậm', 'Tốc độ Internet giảm xuống dưới 50% so với bình thường', 'LOI_HE_THONG', 2, 'DANG_XU_LY', '2024-02-05 09:15:00', false, false, true, 'Đang kiểm tra router và liên hệ ISP',
                                                                                                                                                                                                                     (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'RT001'),
                                                                                                                                                                                                                     (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

                                                                                                                                                                                                                    ('CB2024003', 'Máy photocopy kẹt giấy thường xuyên', 'Máy bị kẹt giấy 5 lần trong ngày', 'LOI_HE_THONG', 4, 'DA_XU_LY', '2024-01-18 11:20:00', false, true, true, 'Đã sửa chữa và thay roller mới',
                                                                                                                                                                                                                     (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'IN002'),
                                                                                                                                                                                                                     (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao')),

                                                                                                                                                                                                                    ('CB2024004', 'Điều hòa phòng 102 không lạnh', 'Điều hòa chạy nhưng không xuất khí lạnh', 'LOI_HE_THONG', 3, 'CHO_XU_LY', '2024-03-15 13:45:00', false, true, false, 'Nghi ngờ thiếu gas hoặc tắc đường ống',
                                                                                                                                                                                                                     (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'DH002'),
                                                                                                                                                                                                                     NULL);

-- 23. HIỆU NĂNG THIẾT BỊ (HIEU_NANG_THIET_BI) 
INSERT INTO hieu_nang_thiet_bi (ngay_do, thoi_gian_hoat_dong, thoi_gian_dung_may, thoi_gian_bao_tri, ty_le_kha_dung, ty_le_hieu_suat, ty_le_chat_luong, oee, ghi_chu, id_thiet_bi, id_nguoi_do) VALUES

-- Hiệu năng máy chiếu Epson
('2024-01-31 17:00:00', 160, 20, 10, 84.21, 95.00, 98.50, 78.67, 'Tháng 1: Hoạt động ổn định',
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MC001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

('2024-02-29 17:00:00', 150, 15, 15, 83.33, 93.00, 97.00, 75.23, 'Tháng 2: Cần vệ sinh',
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MC001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

-- Hiệu năng máy tính Dell
('2024-01-31 17:00:00', 170, 10, 0, 94.44, 98.00, 99.00, 91.70, 'Tháng 1: Hiệu suất cao',
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MT001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

('2024-02-29 17:00:00', 165, 15, 0, 91.67, 96.00, 98.50, 86.67, 'Tháng 2: Vẫn tốt',
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MT001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

-- Hiệu năng điều hòa Daikin
('2024-01-31 17:00:00', 200, 30, 20, 80.00, 90.00, 95.00, 68.40, 'Tháng 1: Mùa đông ít sử dụng',
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'DH001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao')),

('2024-02-29 17:00:00', 180, 25, 15, 81.82, 92.00, 96.00, 72.27, 'Tháng 2: Bắt đầu nóng lên',
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'DH001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao'));

-- 24. THÔNG SỐ THIẾT BỊ (THONG_SO_THIET_BI)
INSERT INTO thong_so_thiet_bi (ten_thong_so, gia_tri_so, gia_tri_van_ban, don_vi, loai_thong_so, gia_tri_min, gia_tri_max, gia_tri_mac_dinh, bat_buoc, hien_thi_tren_giao_dien, co_the_chinh_sua, thu_tu_hien_thi, mo_ta, trang_thai_hoat_dong, id_thiet_bi, id_nguoi_tao) VALUES

-- Thông số máy chiếu Epson
('Độ phân giải', NULL, 'XGA (1024x768)', '', 'VAN_BAN', NULL, NULL, NULL, true, true, false, 1, 'Độ phân giải hiển thị của máy chiếu', true,
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MC001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

('Độ sáng', 3300, NULL, 'lumens', 'SO', 2000, 5000, 3300, true, true, false, 2, 'Độ sáng tối đa của máy chiếu', true,
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MC001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

('Tuổi thọ bóng đèn', 10000, NULL, 'giờ', 'SO', 5000, 15000, 10000, false, true, false, 3, 'Tuổi thọ ước tính của bóng đèn', true,
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MC001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

-- Thông số máy tính Dell
('Bộ vi xử lý', NULL, 'Intel Core i5-10500', '', 'VAN_BAN', NULL, NULL, NULL, true, true, false, 1, 'CPU của máy tính', true,
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MT001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

('RAM', 8, NULL, 'GB', 'SO', 4, 32, 8, true, true, true, 2, 'Dung lượng RAM', true,
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MT001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

('Ổ cứng', 256, NULL, 'GB', 'SO', 128, 2048, 256, true, true, true, 3, 'Dung lượng ổ cứng SSD', true,
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MT001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')),

-- Thông số điều hòa Daikin
('Công suất làm lạnh', 3.5, NULL, 'kW', 'SO', 1.0, 10.0, 3.5, true, true, false, 1, 'Công suất làm lạnh định mức', true,
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'DH001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao')),

('Điện áp hoạt động', 220, NULL, 'V', 'SO', 200, 240, 220, true, true, false, 2, 'Điện áp hoạt động định mức', true,
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'DH001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao')),

('Loại gas', NULL, 'R32', '', 'VAN_BAN', NULL, NULL, NULL, true, true, false, 3, 'Loại gas làm lạnh sử dụng', true,
 (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'DH001'),
 (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao'));

-- 25. CẬP NHẬT QUAN HỆ VÀ TÍNH TOÁN

-- Cập nhật đội bảo trì cho người dùng
UPDATE nguoi_dung SET id_doi_bao_tri = (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT001')
WHERE ten_dang_nhap = 'thanh.vo';

UPDATE nguoi_dung SET id_doi_bao_tri = (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT002')
WHERE ten_dang_nhap = 'hung.dao';

UPDATE nguoi_dung SET id_doi_bao_tri = (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT003')
WHERE ten_dang_nhap = 'duc.le';

-- Cập nhật đội phụ trách cho thiết bị
UPDATE thiet_bi SET id_doi_bao_tri = (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT001')
WHERE ma_thiet_bi IN ('MC001', 'MC002', 'MC003', 'BDT001', 'BDT002', 'MT001', 'MT002', 'LT001', 'RT001', 'SW001', 'AP001');

UPDATE thiet_bi SET id_doi_bao_tri = (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT002')
WHERE ma_thiet_bi IN ('DH001', 'DH002', 'LOA001', 'MIC001');

UPDATE thiet_bi SET id_doi_bao_tri = (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT003')
WHERE ma_thiet_bi IN ('IN001', 'IN002');

-- Cập nhật người phụ trách thiết bị
UPDATE thiet_bi SET id_nguoi_phu_trach = (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')
WHERE ma_thiet_bi IN ('MC001', 'MC002', 'MC003', 'BDT001', 'BDT002', 'MT001', 'MT002', 'LT001', 'RT001', 'SW001', 'AP001');

UPDATE thiet_bi SET id_nguoi_phu_trach = (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao')
WHERE ma_thiet_bi IN ('DH001', 'DH002', 'LOA001', 'MIC001');

UPDATE thiet_bi SET id_nguoi_phu_trach = (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'duc.le')
WHERE ma_thiet_bi IN ('IN001', 'IN002');

-- Cập nhật thời gian bảo trì tiếp theo cho thiết bị
UPDATE thiet_bi SET
                    lan_bao_tri_cuoi = '2024-01-05 09:00:00',
                    lan_bao_tri_tiep_theo = '2024-07-05 09:00:00',
                    so_gio_hoat_dong = 2400,
                    so_lan_bao_tri = 3
WHERE ma_thiet_bi = 'MC001';

UPDATE thiet_bi SET
                    lan_bao_tri_cuoi = '2024-02-15 08:00:00',
                    lan_bao_tri_tiep_theo = '2024-05-15 08:00:00',
                    so_gio_hoat_dong = 1800,
                    so_lan_bao_tri = 2
WHERE ma_thiet_bi = 'DH002';

UPDATE thiet_bi SET
                    lan_bao_tri_cuoi = '2024-03-01 10:00:00',
                    lan_bao_tri_tiep_theo = '2026-03-01 10:00:00',
                    so_gio_hoat_dong = 3200,
                    so_lan_bao_tri = 1
WHERE ma_thiet_bi = 'MT001';

-- 26. THÊM DỮ LIỆU THIẾT BỊ BỔ SUNG

-- Thêm máy chiếu cho các phòng học khác
INSERT INTO thiet_bi (ma_thiet_bi, ten_thiet_bi, mo_ta, hang_san_xuat, model, so_serial, nam_san_xuat, ngay_lap_dat, ngay_dua_vao_su_dung, gia_tri_ban_dau, gia_tri_hien_tai, vi_tri_lap_dat, chu_ky_bao_tri_dinh_ky, trang_thai_hoat_dong, id_nhom_thiet_bi, id_trang_thai) VALUES

                                                                                                                                                                                                                                                                                 ('MC004', 'Máy chiếu ViewSonic PA503W', 'Máy chiếu WXGA 3600 lumens', 'ViewSonic', 'PA503W', 'VS2022001', 2022, '2022-10-15', '2022-10-20', 14000000, 12000000, 'Phòng 103 - Tầng 1', 180, true,
                                                                                                                                                                                                                                                                                  (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_CHIEU'),
                                                                                                                                                                                                                                                                                  (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

                                                                                                                                                                                                                                                                                 ('MC005', 'Máy chiếu Optoma X400', 'Máy chiếu XGA 4000 lumens', 'Optoma', 'X400', 'OP2021001', 2021, '2021-11-10', '2021-11-15', 16000000, 13000000, 'Phòng 104 - Tầng 1', 180, true,
                                                                                                                                                                                                                                                                                  (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_CHIEU'),
                                                                                                                                                                                                                                                                                  (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'BAO_TRI'));

-- Thêm máy tính cho phòng tin học
INSERT INTO thiet_bi (ma_thiet_bi, ten_thiet_bi, mo_ta, hang_san_xuat, model, so_serial, nam_san_xuat, ngay_lap_dat, ngay_dua_vao_su_dung, gia_tri_ban_dau, gia_tri_hien_tai, vi_tri_lap_dat, chu_ky_bao_tri_dinh_ky, trang_thai_hoat_dong, id_nhom_thiet_bi, id_trang_thai) VALUES

                                                                                                                                                                                                                                                                                 ('MT003', 'Máy tính Asus ExpertCenter D7', 'PC để bàn Intel Core i3', 'Asus', 'D700MA', 'AS2023002', 2023, '2023-04-15', '2023-04-20', 16000000, 14500000, 'Phòng tin học 1 - Vị trí 01', 730, true,
                                                                                                                                                                                                                                                                                  (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_TINH'),
                                                                                                                                                                                                                                                                                  (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

                                                                                                                                                                                                                                                                                 ('MT004', 'Máy tính Lenovo ThinkCentre M70q', 'PC mini Intel Core i5', 'Lenovo', 'M70q Gen 3', 'LN2023001', 2023, '2023-05-10', '2023-05-15', 20000000, 18000000, 'Phòng tin học 2 - Vị trí 01', 730, true,
                                                                                                                                                                                                                                                                                  (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_TINH'),
                                                                                                                                                                                                                                                                                  (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG'));

-- Thêm màn hình máy tính
INSERT INTO thiet_bi (ma_thiet_bi, ten_thiet_bi, mo_ta, hang_san_xuat, model, so_serial, nam_san_xuat, ngay_lap_dat, ngay_dua_vao_su_dung, gia_tri_ban_dau, gia_tri_hien_tai, vi_tri_lap_dat, chu_ky_bao_tri_dinh_ky, trang_thai_hoat_dong, id_nhom_thiet_bi, id_trang_thai) VALUES

                                                                                                                                                                                                                                                                                 ('MH001', 'Màn hình Dell 24 inch', 'Màn hình LED 24 inch Full HD', 'Dell', 'S2421HS', 'DL2023003', 2023, '2023-04-15', '2023-04-20', 4500000, 4000000, 'Phòng tin học 1 - Vị trí 01', 1095, true,
                                                                                                                                                                                                                                                                                  (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_TINH'),
                                                                                                                                                                                                                                                                                  (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG')),

                                                                                                                                                                                                                                                                                 ('MH002', 'Màn hình Samsung 22 inch', 'Màn hình LED 22 inch Full HD', 'Samsung', 'F22T450FQE', 'SM2023001', 2023, '2023-05-10', '2023-05-15', 4200000, 3800000, 'Phòng tin học 2 - Vị trí 01', 1095, true,
                                                                                                                                                                                                                                                                                  (SELECT id_nhom_thiet_bi FROM nhom_thiet_bi WHERE ma_nhom = 'MAY_TINH'),
                                                                                                                                                                                                                                                                                  (SELECT id_trang_thai FROM trang_thai_thiet_bi WHERE ma_trang_thai = 'HOAT_DONG'));

-- 27. CẬP NHẬT CHO CÁC THIẾT BỊ MỚI

-- Gán đội bảo trì cho thiết bị mới
UPDATE thiet_bi SET
                    id_doi_bao_tri = (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT001'),
                    id_nguoi_phu_trach = (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo')
WHERE ma_thiet_bi IN ('MC004', 'MC005', 'MT003', 'MT004', 'MH001', 'MH002');

-- 28. THÊM MỘT SỐ YÊU CẦU BẢO TRÌ KHÁC

INSERT INTO yeu_cau_bao_tri (ma_yeu_cau, tieu_de, mo_ta, loai_yeu_cau, muc_do_uu_tien, trang_thai, ngay_yeu_cau, ngay_mong_muon, thoi_gian_du_kien, chi_phi_du_kien, yeu_cau_dung_may, co_anh_huong_san_xuat, can_vat_tu_dac_biet, ghi_chu, id_thiet_bi, id_nguoi_yeu_cau, id_ke_hoach) VALUES

                                                                                                                                                                                                                                                                                            ('YC2024006', 'Vệ sinh màn hình máy tính', 'Màn hình bị bám bụi và vết ố', 'BAO_TRI_DINH_KY', 1, 'CHO_DUYET', '2024-03-20 08:00:00', '2024-03-25 17:00:00', 30, 100000, false, false, false, 'Vệ sinh định kỳ cuối năm học',
                                                                                                                                                                                                                                                                                             (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'MH001'),
                                                                                                                                                                                                                                                                                             (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hoa.le'),
                                                                                                                                                                                                                                                                                             NULL),

                                                                                                                                                                                                                                                                                            ('YC2024007', 'Kiểm tra hệ thống âm thanh hội trường', 'Loa có tiếng rè, micro không rõ', 'KIEM_TRA_AN_TOAN', 3, 'CHO_DUYET', '2024-03-22 10:00:00', '2024-03-28 17:00:00', 240, 0, false, true, false, 'Chuẩn bị cho lễ tốt nghiệp',
                                                                                                                                                                                                                                                                                             (SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = 'LOA001'),
                                                                                                                                                                                                                                                                                             (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'duc.le'),
                                                                                                                                                                                                                                                                                             NULL);

-- =================================================================
-- KẾT THÚC DỮ LIỆU MẪU
-- =================================================================

-- THỐNG KÊ TỔNG QUAN SAU KHI NHẬP DỮ LIỆU
SELECT 'THỐNG KÊ TỔNG QUAN HỆ THỐNG' as title;

SELECT
    'Người dùng' as loai,
    COUNT(*) as so_luong
FROM nguoi_dung
UNION ALL
SELECT
    'Thiết bị' as loai,
    COUNT(*) as so_luong
FROM thiet_bi
UNION ALL
SELECT
    'Vật tư' as loai,
    COUNT(*) as so_luong
FROM vat_tu
UNION ALL
SELECT
    'Yêu cầu bảo trì' as loai,
    COUNT(*) as so_luong
FROM yeu_cau_bao_tri
UNION ALL
SELECT
    'Đội bảo trì' as loai,
    COUNT(*) as so_luong
FROM doi_bao_tri;

-- Thống kê thiết bị theo trạng thái
SELECT
    tt.ten_trang_thai,
    COUNT(tb.id_thiet_bi) as so_luong_thiet_bi
FROM trang_thai_thiet_bi tt
         LEFT JOIN thiet_bi tb ON tt.id_trang_thai = tb.id_trang_thai
GROUP BY tt.id_trang_thai, tt.ten_trang_thai
ORDER BY COUNT(tb.id_thiet_bi) DESC;

-- Thống kê yêu cầu bảo trì theo trạng thái
SELECT
    trang_thai,
    COUNT(*) as so_luong
FROM yeu_cau_bao_tri
GROUP BY trang_thai
ORDER BY so_luong DESC;