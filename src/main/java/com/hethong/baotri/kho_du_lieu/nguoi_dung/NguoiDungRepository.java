package com.hethong.baotri.kho_du_lieu.nguoi_dung;

import com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository cho thực thể NguoiDung
 *
 * @author Đội phát triển hệ thống bảo trì
 * @version 1.0
 */
@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {

    /**
     * Tìm người dùng theo tên đăng nhập
     *
     * @param tenDangNhap tên đăng nhập
     * @return người dùng
     */
    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);

    /**
     * Tìm người dùng theo email
     *
     * @param email email
     * @return người dùng
     */
    Optional<NguoiDung> findByEmail(String email);

    /**
     * Tìm người dùng theo số điện thoại
     *
     * @param soDienThoai số điện thoại
     * @return người dùng
     */
    Optional<NguoiDung> findBySoDienThoai(String soDienThoai);

    /**
     * Kiểm tra tên đăng nhập đã tồn tại chưa
     *
     * @param tenDangNhap tên đăng nhập
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsByTenDangNhap(String tenDangNhap);

    /**
     * Kiểm tra email đã tồn tại chưa
     *
     * @param email email
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsByEmail(String email);

    /**
     * Kiểm tra số điện thoại đã tồn tại chưa
     *
     * @param soDienThoai số điện thoại
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsBySoDienThoai(String soDienThoai);

    /**
     * Tìm người dùng theo trạng thái hoạt động
     *
     * @param trangThaiHoatDong trạng thái hoạt động
     * @param pageable thông tin phân trang
     * @return danh sách người dùng
     */
    Page<NguoiDung> findByTrangThaiHoatDong(Boolean trangThaiHoatDong, Pageable pageable);

    /**
     * Tìm người dùng theo họ và tên (tìm kiếm gần đúng)
     *
     * @param hoVaTen họ và tên
     * @param pageable thông tin phân trang
     * @return danh sách người dùng
     */
    Page<NguoiDung> findByHoVaTenContainingIgnoreCase(String hoVaTen, Pageable pageable);

    /**
     * Tìm người dùng theo vai trò
     *
     * @param tenVaiTro tên vai trò
     * @param pageable thông tin phân trang
     * @return danh sách người dùng
     */
    @Query("SELECT nd FROM NguoiDung nd JOIN nd.vaiTroSet vt WHERE vt.tenVaiTro = :tenVaiTro")
    Page<NguoiDung> findByVaiTro(@Param("tenVaiTro") String tenVaiTro, Pageable pageable);

    /**
     * Tìm người dùng theo đội bảo trì
     *
     * @param idDoiBaoTri ID đội bảo trì
     * @param pageable thông tin phân trang
     * @return danh sách người dùng
     */
    Page<NguoiDung> findByDoiBaoTri_IdDoiBaoTri(Long idDoiBaoTri, Pageable pageable);

    /**
     * Tìm người dùng có quyền cụ thể
     *
     * @param tenQuyen tên quyền
     * @return danh sách người dùng
     */
    @Query("SELECT DISTINCT nd FROM NguoiDung nd JOIN nd.vaiTroSet vt JOIN vt.quyenSet q WHERE q.tenQuyen = :tenQuyen")
    List<NguoiDung> findByQuyen(@Param("tenQuyen") String tenQuyen);

    /**
     * Tìm người dùng đăng nhập trong khoảng thời gian
     *
     * @param tuNgay từ ngày
     * @param denNgay đến ngày
     * @param pageable thông tin phân trang
     * @return danh sách người dùng
     */
    Page<NguoiDung> findByLanDangNhapCuoiBetween(LocalDateTime tuNgay, LocalDateTime denNgay, Pageable pageable);

    /**
     * Tìm người dùng chưa đăng nhập lần nào
     *
     * @param pageable thông tin phân trang
     * @return danh sách người dùng
     */
    Page<NguoiDung> findByLanDangNhapCuoiIsNull(Pageable pageable);

    /**
     * Tìm người dùng bị khóa
     *
     * @param pageable thông tin phân trang
     * @return danh sách người dùng
     */
    Page<NguoiDung> findByTaiKhoanKhongBiKhoa(Boolean taiKhoanKhongBiKhoa, Pageable pageable);

    /**
     * Tìm người dùng có số lần đăng nhập thất bại lớn hơn
     *
     * @param soLan số lần
     * @param pageable thông tin phân trang
     * @return danh sách người dùng
     */
    Page<NguoiDung> findBySoLanDangNhapThatBaiGreaterThan(Integer soLan, Pageable pageable);

    /**
     * Tìm kiếm người dùng theo từ khóa
     *
     * @param tuKhoa từ khóa tìm kiếm
     * @param pageable thông tin phân trang
     * @return danh sách người dùng
     */
    @Query("SELECT nd FROM NguoiDung nd WHERE " +
            "LOWER(nd.tenDangNhap) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR " +
            "LOWER(nd.hoVaTen) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR " +
            "LOWER(nd.email) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR " +
            "LOWER(nd.soDienThoai) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))")
    Page<NguoiDung> timKiemNguoiDung(@Param("tuKhoa") String tuKhoa, Pageable pageable);

    /**
     * Tìm kiếm người dùng nâng cao
     *
     * @param tuKhoa từ khóa
     * @param tenVaiTro tên vai trò
     * @param trangThaiHoatDong trạng thái hoạt động
     * @param idDoiBaoTri ID đội bảo trì
     * @param pageable thông tin phân trang
     * @return danh sách người dùng
     */
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

    /**
     * Đếm số người dùng theo trạng thái hoạt động
     *
     * @param trangThaiHoatDong trạng thái hoạt động
     * @return số lượng người dùng
     */
    long countByTrangThaiHoatDong(Boolean trangThaiHoatDong);

    /**
     * Đếm số người dùng theo vai trò
     *
     * @param tenVaiTro tên vai trò
     * @return số lượng người dùng
     */
    @Query("SELECT COUNT(DISTINCT nd) FROM NguoiDung nd JOIN nd.vaiTroSet vt WHERE vt.tenVaiTro = :tenVaiTro")
    long countByVaiTro(@Param("tenVaiTro") String tenVaiTro);

    /**
     * Tìm người dùng được tạo trong khoảng thời gian
     *
     * @param tuNgay từ ngày
     * @param denNgay đến ngày
     * @param pageable thông tin phân trang
     * @return danh sách người dùng
     */
    Page<NguoiDung> findByNgayTaoBetween(LocalDateTime tuNgay, LocalDateTime denNgay, Pageable pageable);

    /**
     * Tìm người dùng có tài khoản sắp hết hạn khóa
     *
     * @param thoiGian thời gian hiện tại
     * @return danh sách người dùng
     */
    @Query("SELECT nd FROM NguoiDung nd WHERE nd.thoiGianKhoaTaiKhoan IS NOT NULL AND nd.thoiGianKhoaTaiKhoan <= :thoiGian")
    List<NguoiDung> findByThoiGianKhoaTaiKhoanLessThanEqual(@Param("thoiGian") LocalDateTime thoiGian);

    /**
     * Cập nhật trạng thái hoạt động của người dùng
     *
     * @param idNguoiDung ID người dùng
     * @param trangThaiHoatDong trạng thái hoạt động mới
     * @return số dòng được cập nhật
     */
    @Query("UPDATE NguoiDung nd SET nd.trangThaiHoatDong = :trangThaiHoatDong, nd.ngayCapNhat = CURRENT_TIMESTAMP WHERE nd.idNguoiDung = :idNguoiDung")
    int capNhatTrangThaiHoatDong(@Param("idNguoiDung") Long idNguoiDung, @Param("trangThaiHoatDong") Boolean trangThaiHoatDong);

    /**
     * Cập nhật thông tin đăng nhập cuối
     *
     * @param idNguoiDung ID người dùng
     * @param lanDangNhapCuoi lần đăng nhập cuối
     * @return số dòng được cập nhật
     */
    @Query("UPDATE NguoiDung nd SET nd.lanDangNhapCuoi = :lanDangNhapCuoi, nd.soLanDangNhapThatBai = 0 WHERE nd.idNguoiDung = :idNguoiDung")
    int capNhatLanDangNhapCuoi(@Param("idNguoiDung") Long idNguoiDung, @Param("lanDangNhapCuoi") LocalDateTime lanDangNhapCuoi);

    /**
     * Thống kê người dùng theo vai trò
     *
     * @return danh sách thống kê
     */
    @Query("SELECT vt.tenVaiTro, COUNT(DISTINCT nd) FROM NguoiDung nd JOIN nd.vaiTroSet vt GROUP BY vt.tenVaiTro")
    List<Object[]> thongKeNguoiDungTheoVaiTro();

    /**
     * Thống kê người dùng theo đội bảo trì
     *
     * @return danh sách thống kê
     */
    @Query("SELECT dbt.tenDoi, COUNT(nd) FROM NguoiDung nd JOIN nd.doiBaoTri dbt GROUP BY dbt.tenDoi")
    List<Object[]> thongKeNguoiDungTheoDoiBaoTri();

    /**
     * Lấy danh sách người dùng có thể phân công công việc
     *
     * @param tenQuyen tên quyền yêu cầu
     * @return danh sách người dùng
     */
    @Query("SELECT DISTINCT nd FROM NguoiDung nd JOIN nd.vaiTroSet vt JOIN vt.quyenSet q " +
            "WHERE q.tenQuyen = :tenQuyen AND nd.trangThaiHoatDong = true")
    List<NguoiDung> layNguoiDungCoThePhancong(@Param("tenQuyen") String tenQuyen);
}