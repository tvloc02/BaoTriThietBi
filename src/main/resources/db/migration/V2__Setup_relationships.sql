-- =================================================================
-- THIẾT LẬP QUAN HỆ SAU KHI DATALOADER TẠO USER
-- =================================================================

-- 1. CẬP NHẬT TRƯỞNG ĐỘI CHO CÁC ĐỘI BẢO TRÌ
UPDATE doi_bao_tri SET id_truong_doi = (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'thanh.vo') WHERE ma_doi = 'DBT001';
UPDATE doi_bao_tri SET id_truong_doi = (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'hung.dao') WHERE ma_doi = 'DBT002';
UPDATE doi_bao_tri SET id_truong_doi = (SELECT id_nguoi_dung FROM nguoi_dung WHERE ten_dang_nhap = 'duc.le') WHERE ma_doi = 'DBT003';

-- 2. CẬP NHẬT ĐỘI BẢO TRÌ CHO NGƯỜI DÙNG
UPDATE nguoi_dung SET id_doi_bao_tri = (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT001')
WHERE ten_dang_nhap = 'thanh.vo';

UPDATE nguoi_dung SET id_doi_bao_tri = (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT002')
WHERE ten_dang_nhap = 'hung.dao';

UPDATE nguoi_dung SET id_doi_bao_tri = (SELECT id_doi_bao_tri FROM doi_bao_tri WHERE ma_doi = 'DBT003')
WHERE ten_dang_nhap IN ('duc.le', 'mai.pham');

-- 3. THÊM DỮ LIỆU THIẾT BỊ MẪU (nếu cần)
-- Có thể thêm thiết bị mẫu ở đây...