package com.hethong.baotri.dieu_khien;

import com.hethong.baotri.dich_vu.thiet_bi.ThietBiService;
import com.hethong.baotri.dich_vu.vat_tu.VatTuService;
import com.hethong.baotri.dich_vu.bao_tri.YeuCauBaoTriService;
import com.hethong.baotri.dich_vu.doi_bao_tri.DoiBaoTriService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/trang-chu")
@RequiredArgsConstructor
public class TrangChuController {

    private final ThietBiService thietBiService;
    private final VatTuService vatTuService;
    private final YeuCauBaoTriService yeuCauBaoTriService;
    private final DoiBaoTriService doiBaoTriService;

    @GetMapping("/thong-ke-tong-quan")
    public ResponseEntity<Map<String, Object>> layThongKeTongQuan() {
        Map<String, Object> thongKe = new HashMap<>();

        // Thống kê thiết bị
        thongKe.put("soThietBiHoatDong", thietBiService.demThietBiHoatDong());
        thongKe.put("soThietBiCanBaoTri", thietBiService.layThietBiCanBaoTri().size());
        thongKe.put("trungBinhGioHoatDong", thietBiService.tinhTrungBinhGioHoatDong());

        // Thống kê vật tư
        thongKe.put("tongGiaTriTonKho", vatTuService.tinhTongGiaTriTonKho());
        thongKe.put("soVatTuThieuHang", vatTuService.demVatTuThieuHang());
        thongKe.put("soVatTuHetHan", vatTuService.layVatTuHetHan().size());

        // Thống kê yêu cầu bảo trì
        thongKe.put("soYeuCauChoDuyet", yeuCauBaoTriService.demYeuCauTheoTrangThai("CHO_DUYET"));
        thongKe.put("soYeuCauDangThucHien", yeuCauBaoTriService.demYeuCauTheoTrangThai("DANG_THUC_HIEN"));
        thongKe.put("soYeuCauQuaHan", yeuCauBaoTriService.layYeuCauQuaHan().size());

        // Thống kê đội bảo trì
        thongKe.put("soDoiBaoTriHoatDong", doiBaoTriService.demDoiBaoTriHoatDong());
        thongKe.put("tongSoDoiBaoTri", doiBaoTriService.demTongSoDoiBaoTri());

        return ResponseEntity.ok(thongKe);
    }
}