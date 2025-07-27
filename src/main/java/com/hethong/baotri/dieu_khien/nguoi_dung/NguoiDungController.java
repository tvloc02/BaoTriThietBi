package com.hethong.baotri.dieu_khien.nguoi_dung;
/*
import com.hethong.baotri.dich_vu.nguoi_dung.NguoiDungService;
import com.hethong.baotri.dto.nguoi_dung.NguoiDungDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/nguoi-dung")
@RequiredArgsConstructor
public class NguoiDungController {

    private final NguoiDungService nguoiDungService;

    @PostMapping
    @PreAuthorize("hasAuthority('QUAN_LY_NGUOI_DUNG_THEM')")
    public ResponseEntity<NguoiDungDTO> taoNguoiDung(@Valid @RequestBody NguoiDungDTO nguoiDungDTO) {
        NguoiDungDTO nguoiDungMoi = nguoiDungService.taoNguoiDung(nguoiDungDTO);
        return ResponseEntity.ok(nguoiDungMoi);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('QUAN_LY_NGUOI_DUNG_SUA')")
    public ResponseEntity<NguoiDungDTO> capNhatNguoiDung(@PathVariable Long id,
                                                         @Valid @RequestBody NguoiDungDTO nguoiDungDTO) {
        NguoiDungDTO nguoiDungCapNhat = nguoiDungService.capNhatNguoiDung(id, nguoiDungDTO);
        return ResponseEntity.ok(nguoiDungCapNhat);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('QUAN_LY_NGUOI_DUNG_XOA')")
    public ResponseEntity<Void> xoaNguoiDung(@PathVariable Long id) {
        nguoiDungService.xoaNguoiDung(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('QUAN_LY_NGUOI_DUNG_XEM')")
    public ResponseEntity<NguoiDungDTO> layNguoiDung(@PathVariable Long id) {
        NguoiDungDTO nguoiDung = nguoiDungService.timNguoiDungTheoId(id);
        return ResponseEntity.ok(nguoiDung);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('QUAN_LY_NGUOI_DUNG_XEM')")
    public ResponseEntity<Page<NguoiDungDTO>> layDanhSachNguoiDung(Pageable pageable) {
        Page<NguoiDungDTO> danhSach = nguoiDungService.layDanhSachNguoiDung(pageable);
        return ResponseEntity.ok(danhSach);
    }

    @GetMapping("/tim-kiem")
    @PreAuthorize("hasAuthority('QUAN_LY_NGUOI_DUNG_XEM')")
    public ResponseEntity<Page<NguoiDungDTO>> timKiemNguoiDung(@RequestParam String tuKhoa,
                                                               Pageable pageable) {
        Page<NguoiDungDTO> ketQua = nguoiDungService.timKiemNguoiDung(tuKhoa, pageable);
        return ResponseEntity.ok(ketQua);
    }

    @PostMapping("/{id}/gan-vai-tro")
    @PreAuthorize("hasAuthority('QUAN_LY_VAI_TRO_QUYEN')")
    public ResponseEntity<Void> ganVaiTro(@PathVariable Long id, @RequestParam Long idVaiTro) {
        nguoiDungService.ganVaiTro(id, idVaiTro);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/go-vai-tro")
    @PreAuthorize("hasAuthority('QUAN_LY_VAI_TRO_QUYEN')")
    public ResponseEntity<Void> goVaiTro(@PathVariable Long id, @RequestParam Long idVaiTro) {
        nguoiDungService.goVaiTro(id, idVaiTro);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/trang-thai")
    @PreAuthorize("hasAuthority('QUAN_LY_NGUOI_DUNG_SUA')")
    public ResponseEntity<Void> capNhatTrangThai(@PathVariable Long id,
                                                 @RequestParam Boolean trangThaiHoatDong) {
        nguoiDungService.capNhatTrangThaiHoatDong(id, trangThaiHoatDong);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/khoa-tai-khoan")
    @PreAuthorize("hasAuthority('QUAN_LY_NGUOI_DUNG_SUA')")
    public ResponseEntity<Void> khoaTaiKhoan(@PathVariable Long id) {
        nguoiDungService.khoaTaiKhoan(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/mo-khoa-tai-khoan")
    @PreAuthorize("hasAuthority('QUAN_LY_NGUOI_DUNG_SUA')")
    public ResponseEntity<Void> moKhoaTaiKhoan(@PathVariable Long id) {
        nguoiDungService.moKhoaTaiKhoan(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/doi-mat-khau")
    public ResponseEntity<Void> doiMatKhau(@PathVariable Long id,
                                           @RequestParam String matKhauCu,
                                           @RequestParam String matKhauMoi) {
        nguoiDungService.doiMatKhau(id, matKhauCu, matKhauMoi);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reset-mat-khau")
    @PreAuthorize("hasAuthority('QUAN_LY_NGUOI_DUNG_SUA')")
    public ResponseEntity<Void> resetMatKhau(@PathVariable Long id,
                                             @RequestParam String matKhauMoi) {
        nguoiDungService.resetMatKhau(id, matKhauMoi);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/phan-cong")
    @PreAuthorize("hasAuthority('PHAN_CONG_CONG_VIEC')")
    public ResponseEntity<List<NguoiDungDTO>> layNguoiDungCoThePhancong(@RequestParam String tenQuyen) {
        List<NguoiDungDTO> danhSach = nguoiDungService.layNguoiDungCoThePhancong(tenQuyen);
        return ResponseEntity.ok(danhSach);
    }

    @GetMapping("/thong-ke/vai-tro")
    @PreAuthorize("hasAuthority('XEM_BAO_CAO_TONG_HOP')")
    public ResponseEntity<List<Object[]>> thongKeTheoVaiTro() {
        List<Object[]> thongKe = nguoiDungService.thongKeNguoiDungTheoVaiTro();
        return ResponseEntity.ok(thongKe);
    }
}

 */