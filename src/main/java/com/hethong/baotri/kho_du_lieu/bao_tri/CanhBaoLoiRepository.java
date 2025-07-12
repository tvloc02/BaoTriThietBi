package com.hethong.baotri.kho_du_lieu.bao_tri;

import com.hethong.baotri.thuc_the.bao_tri.CanhBaoLoi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CanhBaoLoiRepository extends JpaRepository<CanhBaoLoi, Long> {

    // Tìm theo mức độ nghiêm trọng
    List<CanhBaoLoi> findByMucDoNghiemTrong(String mucDoNghiemTrong);

    // Tìm theo trạng thái
    Page<CanhBaoLoi> findByTrangThai(String trangThai, Pageable pageable);

    // Tìm theo thiết bị
    Page<CanhBaoLoi> findByThietBi_IdThietBi(Long idThietBi, Pageable pageable);

    // Tìm cảnh báo chưa xử lý
    @Query("SELECT cb FROM CanhBaoLoi cb WHERE cb.trangThai = 'CHUA_XU_LY' ORDER BY cb.mucDoUuTien DESC, cb.ngayTao ASC")
    List<CanhBaoLoi> findCanhBaoChuaXuLy();

    // Tìm cảnh báo trong khoảng thời gian
    @Query("SELECT cb FROM CanhBaoLoi cb WHERE cb.ngayTao BETWEEN :tuNgay AND :denNgay")
    List<CanhBaoLoi> findByNgayTaoBetween(@Param("tuNgay") LocalDateTime tuNgay, @Param("denNgay") LocalDateTime denNgay);

    // Đếm cảnh báo theo trạng thái
    long countByTrangThai(String trangThai);



    // Thống kê theo mức độ nghiêm trọng
    @Query("SELECT cb.mucDoNghiemTrong, COUNT(cb) FROM CanhBaoLoi cb GROUP BY cb.mucDoNghiemTrong")
    List<Object[]> thongKeTheoMucDoNghiemTrong();
}