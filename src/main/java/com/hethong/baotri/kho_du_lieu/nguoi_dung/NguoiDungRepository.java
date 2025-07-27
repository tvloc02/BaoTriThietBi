package com.hethong.baotri.kho_du_lieu.nguoi_dung;

import com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {


    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);

    Optional<NguoiDung> findByEmail(String email);

    Optional<NguoiDung> findBySoDienThoai(String soDienThoai);

    boolean existsByTenDangNhap(String tenDangNhap);

    boolean existsByEmail(String email);

    boolean existsBySoDienThoai(String soDienThoai);

    Page<NguoiDung> findByTrangThaiHoatDong(Boolean trangThaiHoatDong, Pageable pageable);

    Page<NguoiDung> findByHoVaTenContainingIgnoreCase(String hoVaTen, Pageable pageable);

    @Query("SELECT nd FROM NguoiDung nd JOIN nd.vaiTroSet vt WHERE vt.tenVaiTro = :tenVaiTro")
    Page<NguoiDung> findByVaiTro(@Param("tenVaiTro") String tenVaiTro, Pageable pageable);

    Page<NguoiDung> findByDoiBaoTri_IdDoiBaoTri(Long idDoiBaoTri, Pageable pageable);

    @Query("SELECT DISTINCT nd FROM NguoiDung nd JOIN nd.vaiTroSet vt JOIN vt.quyenSet q WHERE q.tenQuyen = :tenQuyen")
    List<NguoiDung> findByQuyen(@Param("tenQuyen") String tenQuyen);

    Page<NguoiDung> findByLanDangNhapCuoiBetween(LocalDateTime tuNgay, LocalDateTime denNgay, Pageable pageable);

    Page<NguoiDung> findByLanDangNhapCuoiIsNull(Pageable pageable);


    Page<NguoiDung> findByTaiKhoanKhongBiKhoa(Boolean taiKhoanKhongBiKhoa, Pageable pageable);


    Page<NguoiDung> findBySoLanDangNhapThatBaiGreaterThan(Integer soLan, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM nguoi_dung_vai_tro WHERE id_nguoi_dung = ?1", nativeQuery = true)
    void deleteUserRoles(Long userId);

    @Modifying
    @Query(value = "INSERT INTO nguoi_dung_vai_tro (id_nguoi_dung, id_vai_tro) VALUES (?1, ?2)", nativeQuery = true)
    void insertUserRole(Long userId, Long roleId);


    @Query("SELECT nd FROM NguoiDung nd WHERE " +
            "LOWER(nd.tenDangNhap) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR " +
            "LOWER(nd.hoVaTen) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR " +
            "LOWER(nd.email) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR " +
            "LOWER(nd.soDienThoai) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))")
    Page<NguoiDung> timKiemNguoiDung(@Param("tuKhoa") String tuKhoa, Pageable pageable);


    @Query("SELECT DISTINCT nd FROM NguoiDung nd " +
            "LEFT JOIN nd.vaiTroSet vt " +
            "LEFT JOIN nd.doiBaoTri dbt " +
            "WHERE (:tuKhoa IS NULL OR " +
            "       LOWER(nd.tenDangNhap) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR " +
            "       LOWER(nd.hoVaTen) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR " +
            "       LOWER(nd.email) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))) " +
            "AND (:tenVaiTro IS NULL OR vt.tenVaiTro = :tenVaiTro) " +
            "AND (:trangThaiHoatDong IS NULL OR nd.trangThaiHoatDong = :trangThaiHoatDong) " +
            "AND (:idDoiBaoTri IS NULL OR dbt.idDoiBaoTri = :idDoiBaoTri)")
    Page<NguoiDung> timKiemNangCao(@Param("tuKhoa") String tuKhoa,
                                   @Param("tenVaiTro") String tenVaiTro,
                                   @Param("trangThaiHoatDong") Boolean trangThaiHoatDong,
                                   @Param("idDoiBaoTri") Long idDoiBaoTri,
                                   Pageable pageable);


    long countByTrangThaiHoatDong(Boolean trangThaiHoatDong);

    @Query("SELECT COUNT(DISTINCT nd) FROM NguoiDung nd JOIN nd.vaiTroSet vt WHERE vt.tenVaiTro = :tenVaiTro")
    long countByVaiTro(@Param("tenVaiTro") String tenVaiTro);

    Page<NguoiDung> findByNgayTaoBetween(LocalDateTime tuNgay, LocalDateTime denNgay, Pageable pageable);

    @Query("SELECT nd FROM NguoiDung nd WHERE nd.thoiGianKhoaTaiKhoan IS NOT NULL AND nd.thoiGianKhoaTaiKhoan <= :thoiGian")
    List<NguoiDung> findByThoiGianKhoaTaiKhoanLessThanEqual(@Param("thoiGian") LocalDateTime thoiGian);

    @Query("UPDATE NguoiDung nd SET nd.trangThaiHoatDong = :trangThaiHoatDong, nd.ngayCapNhat = CURRENT_TIMESTAMP WHERE nd.idNguoiDung = :idNguoiDung")
    int capNhatTrangThaiHoatDong(@Param("idNguoiDung") Long idNguoiDung, @Param("trangThaiHoatDong") Boolean trangThaiHoatDong);

    @Query("UPDATE NguoiDung nd SET nd.lanDangNhapCuoi = :lanDangNhapCuoi, nd.soLanDangNhapThatBai = 0 WHERE nd.idNguoiDung = :idNguoiDung")
    int capNhatLanDangNhapCuoi(@Param("idNguoiDung") Long idNguoiDung, @Param("lanDangNhapCuoi") LocalDateTime lanDangNhapCuoi);

    @Query("SELECT vt.tenVaiTro, COUNT(DISTINCT nd) FROM NguoiDung nd JOIN nd.vaiTroSet vt GROUP BY vt.tenVaiTro")
    List<Object[]> thongKeNguoiDungTheoVaiTro();

    @Query("SELECT dbt.tenDoi, COUNT(nd) FROM NguoiDung nd JOIN nd.doiBaoTri dbt GROUP BY dbt.tenDoi")
    List<Object[]> thongKeNguoiDungTheoDoiBaoTri();

    @Query("SELECT DISTINCT nd FROM NguoiDung nd JOIN nd.vaiTroSet vt JOIN vt.quyenSet q " +
            "WHERE q.tenQuyen = :tenQuyen AND nd.trangThaiHoatDong = true")
    List<NguoiDung> layNguoiDungCoThePhancong(@Param("tenQuyen") String tenQuyen);
}