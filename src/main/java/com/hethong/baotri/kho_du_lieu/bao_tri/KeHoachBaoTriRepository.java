package com.hethong.baotri.kho_du_lieu.bao_tri;

import com.hethong.baotri.thuc_the.bao_tri.KeHoachBaoTri;
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
public interface KeHoachBaoTriRepository extends JpaRepository<KeHoachBaoTri, Long> {

    // Tìm theo mã kế hoạch
    Optional<KeHoachBaoTri> findByMaKeHoach(String maKeHoach);

    // Kiểm tra mã kế hoạch tồn tại
    boolean existsByMaKeHoach(String maKeHoach);

    // Tìm theo trạng thái
    Page<KeHoachBaoTri> findByTrangThai(String trangThai, Pageable pageable);

    // Tìm theo loại kế hoạch
    List<KeHoachBaoTri> findByLoaiKeHoach(String loaiKeHoach);

    // Tìm theo đội bảo trì
    Page<KeHoachBaoTri> findByDoiBaoTri_IdDoiBaoTri(Long idDoiBaoTri, Pageable pageable);

    // Tìm kế hoạch theo thời gian
    @Query("SELECT kh FROM KeHoachBaoTri kh WHERE kh.ngayBatDau BETWEEN :tuNgay AND :denNgay")
    List<KeHoachBaoTri> findByNgayBatDauBetween(@Param("tuNgay") LocalDateTime tuNgay, @Param("denNgay") LocalDateTime denNgay);

    // Tìm kế hoạch ưu tiên
    @Query("SELECT kh FROM KeHoachBaoTri kh WHERE kh.mucDoUuTien >= :mucDo AND kh.trangThai != 'COMPLETED'")
    List<KeHoachBaoTri> findKeHoachUuTien(@Param("mucDo") Integer mucDo);

    // Đếm theo trạng thái
    long countByTrangThai(String trangThai);

    // Tìm kiếm kế hoạch
    @Query("SELECT kh FROM KeHoachBaoTri kh WHERE " +
            "LOWER(kh.tenKeHoach) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR " +
            "LOWER(kh.maKeHoach) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))")
    Page<KeHoachBaoTri> timKiemKeHoach(@Param("tuKhoa") String tuKhoa, Pageable pageable);
}