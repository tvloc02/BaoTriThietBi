-- Xóa dữ liệu cũ có thể bị conflict
DELETE FROM nguoi_dung_vai_tro WHERE id_nguoi_dung IN (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'admin');
DELETE FROM nguoi_dung WHERE ten_dang_nhap = 'admin';

-- DataLoader sẽ tạo lại user admin với password đúng