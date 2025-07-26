-- V3__Fix_all_users_password.sql
-- Sửa password cho tất cả user để giống admin (password: 123456)

-- Lấy password hash từ admin (user đã đăng nhập được)
-- Và áp dụng cho tất cả user khác

UPDATE nguoi_dung
SET mat_khau = (
    SELECT mat_khau
    FROM nguoi_dung AS admin_user
    WHERE admin_user.ten_dang_nhap = 'admin'
    LIMIT 1
    ),
    trang_thai_hoat_dong = true,
    tai_khoan_khong_bi_khoa = true,
    so_lan_dang_nhap_that_bai = 0
WHERE ten_dang_nhap IN (
    'hieupho.nguyen',
    'phong.tran',
    'duc.le',
    'mai.pham',
    'thanh.vo',
    'hung.dao',
    'linh.nguyen',
    'minh.tran',
    'hoa.le'
    );

-- Kiểm tra kết quả - chỉ check những cột tồn tại
SELECT
    ten_dang_nhap,
    ho_va_ten,
    CASE
        WHEN mat_khau = (SELECT mat_khau FROM nguoi_dung WHERE ten_dang_nhap = 'admin')
            THEN 'PASSWORD GIỐNG ADMIN'
        ELSE 'PASSWORD KHÁC ADMIN'
        END as password_status,
    trang_thai_hoat_dong,
    tai_khoan_khong_bi_khoa,
    so_lan_dang_nhap_that_bai
FROM nguoi_dung
ORDER BY ten_dang_nhap;