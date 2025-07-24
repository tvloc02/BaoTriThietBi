package com.hethong.baotri.kho_du_lieu.bao_tri;

import com.hethong.baotri.thuc_the.bao_tri.YeuCauBaoTri;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface YeuCauBaoTriRepository extends JpaRepository<YeuCauBaoTri, Long> {

    boolean existsByMaYeuCau(String maYeuCau);

    Page<YeuCauBaoTri> findByTrangThai(String trangThai, Pageable pageable);

    Page<YeuCauBaoTri> findByLoaiYeuCau(String loaiYeuCau, Pageable pageable);

    Page<YeuCauBaoTri> findByNguoiYeuCau_IdNguoiDung(Long idNguoiDung, Pageable pageable);

    Page<YeuCauBaoTri> findByThietBi_IdThietBi(Long idThietBi, Pageable pageable);

    @Query("SELECT yc FROM YeuCauBaoTri yc WHERE yc.trangThai = :trangThai AND yc.mucDoUuTien >= :mucDoUuTien")
    List<YeuCauBaoTri> findYeuCauUuTien(@Param("trangThai") String trangThai, @Param("mucDoUuTien") Integer mucDoUuTien);

    @Query("SELECT yc FROM YeuCauBaoTri yc WHERE yc.ngayMongMuon <= :ngayHienTai AND yc.trangThai != 'HOAN_THANH'")
    List<YeuCauBaoTri> findYeuCauQuaHan(@Param("ngayHienTai") LocalDateTime ngayHienTai);

    @Query("SELECT COUNT(yc) FROM YeuCauBaoTri yc WHERE yc.trangThai = :trangThai")
    long demYeuCauTheoTrangThai(@Param("trangThai") String trangThai);

    @Query("SELECT yc.trangThai, COUNT(yc) FROM YeuCauBaoTri yc GROUP BY yc.trangThai")
    List<Object[]> thongKeYeuCauTheoTrangThai();
}