package com.hethong.baotri.dich_vu.bao_tri;

import com.hethong.baotri.kho_du_lieu.bao_tri.YeuCauBaoTriRepository;
import com.hethong.baotri.kho_du_lieu.thiet_bi.ThietBiRepository;
import com.hethong.baotri.kho_du_lieu.nguoi_dung.NguoiDungRepository;
import com.hethong.baotri.thuc_the.bao_tri.YeuCauBaoTri;
import com.hethong.baotri.thuc_the.thiet_bi.ThietBi;
import com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung;
import com.hethong.baotri.ngoai_le.NgoaiLeBaoTri;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class YeuCauBaoTriService {

    private final YeuCauBaoTriRepository yeuCauBaoTriRepository;
    private final ThietBiRepository thietBiRepository;
    private final NguoiDungRepository nguoiDungRepository;

    public YeuCauBaoTri taoYeuCauBaoTri(YeuCauBaoTri yeuCau) {
        log.info("Đang tạo yêu cầu bảo trì mới: {}", yeuCau.getMaYeuCau());

        if (yeuCauBaoTriRepository.existsByMaYeuCau(yeuCau.getMaYeuCau())) {
            throw new NgoaiLeBaoTri("Mã yêu cầu đã tồn tại: " + yeuCau.getMaYeuCau());
        }

        return yeuCauBaoTriRepository.save(yeuCau);
    }

    public YeuCauBaoTri duyetYeuCau(Long idYeuCau, Long idNguoiDuyet) {
        log.info("Đang duyệt yêu cầu bảo trì ID: {}", idYeuCau);

        YeuCauBaoTri yeuCau = yeuCauBaoTriRepository.findById(idYeuCau)
                .orElseThrow(() -> new NgoaiLeBaoTri("Không tìm thấy yêu cầu với ID: " + idYeuCau));

        NguoiDung nguoiDuyet = nguoiDungRepository.findById(idNguoiDuyet)
                .orElseThrow(() -> new NgoaiLeBaoTri("Không tìm thấy người duyệt với ID: " + idNguoiDuyet));

        yeuCau.duyetYeuCau(nguoiDuyet);
        return yeuCauBaoTriRepository.save(yeuCau);
    }

    public YeuCauBaoTri tuChoiYeuCau(Long idYeuCau, Long idNguoiDuyet, String lyDo) {
        log.info("Đang từ chối yêu cầu bảo trì ID: {}", idYeuCau);

        YeuCauBaoTri yeuCau = yeuCauBaoTriRepository.findById(idYeuCau)
                .orElseThrow(() -> new NgoaiLeBaoTri("Không tìm thấy yêu cầu với ID: " + idYeuCau));

        NguoiDung nguoiDuyet = nguoiDungRepository.findById(idNguoiDuyet)
                .orElseThrow(() -> new NgoaiLeBaoTri("Không tìm thấy người duyệt với ID: " + idNguoiDuyet));

        yeuCau.tuChoiYeuCau(nguoiDuyet, lyDo);
        return yeuCauBaoTriRepository.save(yeuCau);
    }

    @Transactional(readOnly = true)
    public Page<YeuCauBaoTri> layDanhSachYeuCau(Pageable pageable) {
        return yeuCauBaoTriRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<YeuCauBaoTri> layYeuCauTheoTrangThai(String trangThai, Pageable pageable) {
        return yeuCauBaoTriRepository.findByTrangThai(trangThai, pageable);
    }

    @Transactional(readOnly = true)
    public List<YeuCauBaoTri> layYeuCauUuTien(String trangThai, Integer mucDoUuTien) {
        return yeuCauBaoTriRepository.findYeuCauUuTien(trangThai, mucDoUuTien);
    }

    @Transactional(readOnly = true)
    public List<YeuCauBaoTri> layYeuCauQuaHan() {
        return yeuCauBaoTriRepository.findYeuCauQuaHan(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<Object[]> thongKeYeuCauTheoTrangThai() {
        return yeuCauBaoTriRepository.thongKeYeuCauTheoTrangThai();
    }

    @Transactional(readOnly = true)
    public long demYeuCauTheoTrangThai(String trangThai) {
        return yeuCauBaoTriRepository.demYeuCauTheoTrangThai(trangThai);
    }

    @Transactional(readOnly = true)
    public Optional<YeuCauBaoTri> timYeuCauTheoId(Long idYeuCau) {
        return yeuCauBaoTriRepository.findById(idYeuCau);
    }

    public YeuCauBaoTri batDauThucHien(Long idYeuCau, Long idNguoiThucHien) {
        log.info("Đang bắt đầu thực hiện yêu cầu bảo trì ID: {}", idYeuCau);

        YeuCauBaoTri yeuCau = yeuCauBaoTriRepository.findById(idYeuCau)
                .orElseThrow(() -> new NgoaiLeBaoTri("Không tìm thấy yêu cầu với ID: " + idYeuCau));

        NguoiDung nguoiThucHien = nguoiDungRepository.findById(idNguoiThucHien)
                .orElseThrow(() -> new NgoaiLeBaoTri("Không tìm thấy người thực hiện với ID: " + idNguoiThucHien));

        yeuCau.batDauThucHien(nguoiThucHien);
        return yeuCauBaoTriRepository.save(yeuCau);
    }

    public YeuCauBaoTri hoanThanhYeuCau(Long idYeuCau, Integer thoiGianThucTe, java.math.BigDecimal chiPhiThucTe) {
        log.info("Đang hoàn thành yêu cầu bảo trì ID: {}", idYeuCau);

        YeuCauBaoTri yeuCau = yeuCauBaoTriRepository.findById(idYeuCau)
                .orElseThrow(() -> new NgoaiLeBaoTri("Không tìm thấy yêu cầu với ID: " + idYeuCau));

        yeuCau.hoanThanh(thoiGianThucTe, chiPhiThucTe);
        return yeuCauBaoTriRepository.save(yeuCau);
    }
}