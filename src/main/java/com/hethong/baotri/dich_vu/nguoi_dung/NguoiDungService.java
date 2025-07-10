package com.hethong.baotri.dich_vu.nguoi_dung;

import com.hethong.baotri.dto.nguoi_dung.NguoiDungDTO;
import com.hethong.baotri.kho_du_lieu.nguoi_dung.NguoiDungRepository;
import com.hethong.baotri.kho_du_lieu.nguoi_dung.VaiTroRepository;
import com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung;
import com.hethong.baotri.thuc_the.nguoi_dung.VaiTro;
import com.hethong.baotri.ngoai_le.NgoaiLeNguoiDung;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NguoiDungService implements UserDetailsService {

    private final NguoiDungRepository nguoiDungRepository;
    private final VaiTroRepository vaiTroRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));
    }

    public NguoiDungDTO taoNguoiDung(NguoiDungDTO nguoiDungDTO) {
        log.info("Đang tạo người dùng mới: {}", nguoiDungDTO.getTenDangNhap());

        // Kiểm tra tên đăng nhập đã tồn tại chưa
        if (nguoiDungRepository.existsByTenDangNhap(nguoiDungDTO.getTenDangNhap())) {
            throw new NgoaiLeNguoiDung("Tên đăng nhập đã tồn tại: " + nguoiDungDTO.getTenDangNhap());
        }

        // Kiểm tra email đã tồn tại chưa
        if (nguoiDungDTO.getEmail() != null && nguoiDungRepository.existsByEmail(nguoiDungDTO.getEmail())) {
            throw new NgoaiLeNguoiDung("Email đã tồn tại: " + nguoiDungDTO.getEmail());
        }

        NguoiDung nguoiDung = modelMapper.map(nguoiDungDTO, NguoiDung.class);
        nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDungDTO.getMatKhau()));

        nguoiDung = nguoiDungRepository.save(nguoiDung);
        return modelMapper.map(nguoiDung, NguoiDungDTO.class);
    }

    public NguoiDungDTO capNhatNguoiDung(Long id, NguoiDungDTO nguoiDungDTO) {
        log.info("Đang cập nhật người dùng ID: {}", id);

        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng với ID: " + id));

        // Kiểm tra email đã tồn tại chưa (nếu thay đổi)
        if (nguoiDungDTO.getEmail() != null &&
                !nguoiDungDTO.getEmail().equals(nguoiDung.getEmail()) &&
                nguoiDungRepository.existsByEmail(nguoiDungDTO.getEmail())) {
            throw new NgoaiLeNguoiDung("Email đã tồn tại: " + nguoiDungDTO.getEmail());
        }

        nguoiDung.setHoVaTen(nguoiDungDTO.getHoVaTen());
        nguoiDung.setEmail(nguoiDungDTO.getEmail());
        nguoiDung.setSoDienThoai(nguoiDungDTO.getSoDienThoai());
        nguoiDung.setDiaChi(nguoiDungDTO.getDiaChi());

        nguoiDung = nguoiDungRepository.save(nguoiDung);
        return modelMapper.map(nguoiDung, NguoiDungDTO.class);
    }

    public void xoaNguoiDung(Long id) {
        log.info("Đang xóa người dùng ID: {}", id);

        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng với ID: " + id));

        nguoiDungRepository.delete(nguoiDung);
    }

    @Transactional(readOnly = true)
    public NguoiDungDTO timNguoiDungTheoId(Long id) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng với ID: " + id));

        return modelMapper.map(nguoiDung, NguoiDungDTO.class);
    }

    @Transactional(readOnly = true)
    public Optional<NguoiDungDTO> timNguoiDungTheoTenDangNhap(String tenDangNhap) {
        return nguoiDungRepository.findByTenDangNhap(tenDangNhap)
                .map(nguoiDung -> modelMapper.map(nguoiDung, NguoiDungDTO.class));
    }

    @Transactional(readOnly = true)
    public Page<NguoiDungDTO> layDanhSachNguoiDung(Pageable pageable) {
        return nguoiDungRepository.findAll(pageable)
                .map(nguoiDung -> modelMapper.map(nguoiDung, NguoiDungDTO.class));
    }

    @Transactional(readOnly = true)
    public Page<NguoiDungDTO> timKiemNguoiDung(String tuKhoa, Pageable pageable) {
        return nguoiDungRepository.timKiemNguoiDung(tuKhoa, pageable)
                .map(nguoiDung -> modelMapper.map(nguoiDung, NguoiDungDTO.class));
    }

    public void ganVaiTro(Long idNguoiDung, Long idVaiTro) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(idNguoiDung)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng với ID: " + idNguoiDung));

        VaiTro vaiTro = vaiTroRepository.findById(idVaiTro)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy vai trò với ID: " + idVaiTro));

        nguoiDung.themVaiTro(vaiTro);
        nguoiDungRepository.save(nguoiDung);
    }

    public void goVaiTro(Long idNguoiDung, Long idVaiTro) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(idNguoiDung)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng với ID: " + idNguoiDung));

        VaiTro vaiTro = vaiTroRepository.findById(idVaiTro)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy vai trò với ID: " + idVaiTro));

        nguoiDung.xoaVaiTro(vaiTro);
        nguoiDungRepository.save(nguoiDung);
    }

    public void capNhatTrangThaiHoatDong(Long id, Boolean trangThaiHoatDong) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng với ID: " + id));

        nguoiDung.setTrangThaiHoatDong(trangThaiHoatDong);
        nguoiDungRepository.save(nguoiDung);
    }

    public void khoaTaiKhoan(Long id) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng với ID: " + id));

        nguoiDung.setTaiKhoanKhongBiKhoa(false);
        nguoiDung.setThoiGianKhoaTaiKhoan(LocalDateTime.now().plusDays(30));
        nguoiDungRepository.save(nguoiDung);
    }

    public void moKhoaTaiKhoan(Long id) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng với ID: " + id));

        nguoiDung.moKhoaTaiKhoan();
        nguoiDungRepository.save(nguoiDung);
    }

    public void doiMatKhau(Long id, String matKhauCu, String matKhauMoi) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng với ID: " + id));

        if (!passwordEncoder.matches(matKhauCu, nguoiDung.getMatKhau())) {
            throw new NgoaiLeNguoiDung("Mật khẩu cũ không đúng");
        }

        nguoiDung.setMatKhau(passwordEncoder.encode(matKhauMoi));
        nguoiDungRepository.save(nguoiDung);
    }

    public void resetMatKhau(Long id, String matKhauMoi) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng với ID: " + id));

        nguoiDung.setMatKhau(passwordEncoder.encode(matKhauMoi));
        nguoiDungRepository.save(nguoiDung);
    }

    public void capNhatLanDangNhapCuoi(String tenDangNhap) {
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new NgoaiLeNguoiDung("Không tìm thấy người dùng: " + tenDangNhap));

        nguoiDung.capNhatLanDangNhapCuoi();
        nguoiDungRepository.save(nguoiDung);
    }

    public void xuLyDangNhapThatBai(String tenDangNhap) {
        Optional<NguoiDung> nguoiDung = nguoiDungRepository.findByTenDangNhap(tenDangNhap);
        if (nguoiDung.isPresent()) {
            nguoiDung.get().tangSoLanDangNhapThatBai();
            nguoiDungRepository.save(nguoiDung.get());
        }
    }

    @Transactional(readOnly = true)
    public List<NguoiDungDTO> layNguoiDungCoThePhancong(String tenQuyen) {
        return nguoiDungRepository.layNguoiDungCoThePhancong(tenQuyen)
                .stream()
                .map(nguoiDung -> modelMapper.map(nguoiDung, NguoiDungDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Object[]> thongKeNguoiDungTheoVaiTro() {
        return nguoiDungRepository.thongKeNguoiDungTheoVaiTro();
    }
}