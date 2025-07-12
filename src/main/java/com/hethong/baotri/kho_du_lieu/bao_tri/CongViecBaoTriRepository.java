package com.hethong.baotri.kho_du_lieu.bao_tri;

import com.hethong.baotri.thuc_the.bao_tri.CongViecBaoTri;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CongViecBaoTriRepository extends JpaRepository<CongViecBaoTri, Long> {

    // Tìm theo mã công việc
    Optional<CongViecBaoTri> findByMaCongViec(String maCongViec);

    // Kiểm tra mã công việc tồn tại
    boolean existsByMaCongViec(String maCongViec);

    // Tìm theo trạng thái
    Page<CongViecBaoTri> findByTrangThai(String trangThai, Pageable pageable);

    // Tìm theo loại công việc
    List<CongViecBaoTri> findByLoaiCongViec(String loaiCongViec);

    // Tìm theo người thực hiện
    Page<CongViecBaoTri> findByNguoiThucHien_IdNguoiDung(Long idNguoiDung, Pageable pageable);

    // Tìm theo thiết bị
    Page<CongViecBaoTri> findByThietBi_IdThietBi(Long idThietBi, Pageable pageable);

    // Tìm theo kế hoạch bảo trì
    List<CongViecBaoTri> findByKeHoachBaoTri_IdKeHoach(Long idKeHoach);

    // Tìm công việc ưu tiên
    @Query("SELECT cv FROM CongViecBaoTri cv WHERE cv.mucDoUuTien >= :mucDo AND cv.trangThai != 'COMPLETED'")
    List<CongViecBaoTri> findCongViecUuTien(@Param("mucDo") Integer mucDo);

    // Tìm công việc quá hạn
    @Query("SELECT cv FROM CongViecBaoTri cv WHERE cv.ngayKetThucDuKien < :ngayHienTai AND cv.trangThai != 'COMPLETED'")
    List<CongViecBaoTri> findCongViecQuaHan(@Param("ngayHienTai") LocalDateTime ngayHienTai);

    // Đếm theo trạng thái
    long countByTrangThai(String trangThai);

    // Tìm kiếm công việc
    @Query("SELECT cv FROM CongViecBaoTri cv WHERE " +
            "LOWER(cv.tenCongViec) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))")
    Page<CongViecBaoTri> timKiemCongViec(@Param("tuKhoa") String tuKhoa, Pageable pageable);

    // Thống kê theo trạng thái
    @Query("SELECT cv.trangThai, COUNT(cv) FROM CongViecBaoTri cv GROUP BY cv.trangThai")
    List<Object[]> thongKeTheoTrangThai();
}