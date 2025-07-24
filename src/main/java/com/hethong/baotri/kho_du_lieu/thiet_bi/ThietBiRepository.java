package com.hethong.baotri.kho_du_lieu.thiet_bi;

import com.hethong.baotri.thuc_the.thiet_bi.ThietBi;
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
public interface ThietBiRepository extends JpaRepository<ThietBi, Long> {

    Optional<ThietBi> findByMaThietBi(String maThietBi);

    boolean existsByMaThietBi(String maThietBi);

    Page<ThietBi> findByTrangThaiHoatDong(Boolean trangThaiHoatDong, Pageable pageable);

    Page<ThietBi> findByTenThietBiContainingIgnoreCase(String tenThietBi, Pageable pageable);

    Page<ThietBi> findByNhomThietBi_IdNhomThietBi(Long idNhomThietBi, Pageable pageable);

    Page<ThietBi> findByTrangThaiThietBi_IdTrangThai(Long idTrangThai, Pageable pageable);

    @Query("SELECT tb FROM ThietBi tb WHERE tb.lanBaoTriTiepTheo <= :ngayHienTai")
    List<ThietBi> findThietBiCanBaoTri(@Param("ngayHienTai") LocalDateTime ngayHienTai);

    @Query("SELECT tb FROM ThietBi tb WHERE tb.lanBaoTriTiepTheo BETWEEN :tuNgay AND :denNgay")
    List<ThietBi> findThietBiCanBaoTriTrongKhoang(@Param("tuNgay") LocalDateTime tuNgay,
                                                  @Param("denNgay") LocalDateTime denNgay);

    @Query("SELECT tb FROM ThietBi tb WHERE " +
            "LOWER(tb.tenThietBi) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR " +
            "LOWER(tb.maThietBi) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))")
    Page<ThietBi> timKiemThietBi(@Param("tuKhoa") String tuKhoa, Pageable pageable);

    @Query("SELECT COUNT(tb) FROM ThietBi tb WHERE tb.trangThaiHoatDong = true")
    long demThietBiHoatDong();

    @Query("SELECT AVG(tb.soGioHoatDong) FROM ThietBi tb WHERE tb.trangThaiHoatDong = true")
    Double tinhTrungBinhGioHoatDong();
}