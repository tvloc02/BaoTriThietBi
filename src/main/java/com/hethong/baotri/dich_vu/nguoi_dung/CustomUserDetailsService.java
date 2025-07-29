// ✅ CẬP NHẬT FILE: com/hethong/baotri/dich_vu/nguoi_dung/CustomUserDetailsService.java

package com.hethong.baotri.dich_vu.nguoi_dung;

import com.hethong.baotri.kho_du_lieu.nguoi_dung.NguoiDungRepository;
import com.hethong.baotri.thuc_the.nguoi_dung.NguoiDung;
import com.hethong.baotri.thuc_the.nguoi_dung.VaiTro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final NguoiDungRepository nguoiDungRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("🔍 Loading user details for username: [{}]", username);

        // ✅ Tìm user trong database
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> {
                    log.error("❌ User not found: [{}]", username);
                    return new UsernameNotFoundException("Không tìm thấy người dùng: " + username);
                });

        log.info("👤 Found user: [{}] - Active: [{}]", nguoiDung.getTenDangNhap(), nguoiDung.getTrangThaiHoatDong());

        // ✅ Load authorities từ vai trò
        Collection<? extends GrantedAuthority> authorities = getAuthorities(nguoiDung);

        log.info("🎭 Loaded authorities for [{}]: {}", username, authorities);

        // ✅ Cập nhật lần đăng nhập cuối - DIRECTLY trong repository
        try {
            nguoiDung.capNhatLanDangNhapCuoi();
            nguoiDungRepository.save(nguoiDung);
            log.debug("✅ Updated last login time for user: [{}]", username);
        } catch (Exception e) {
            log.warn("⚠️ Could not update last login time: {}", e.getMessage());
        }

        // ✅ Tạo UserDetails
        return User.builder()
                .username(nguoiDung.getTenDangNhap())
                .password(nguoiDung.getMatKhau())
                .authorities(authorities)
                .accountExpired(!nguoiDung.getTaiKhoanKhongHetHan())
                .accountLocked(!nguoiDung.getTaiKhoanKhongBiKhoa())
                .credentialsExpired(!nguoiDung.getThongTinDangNhapHopLe())
                .disabled(!nguoiDung.getTrangThaiHoatDong())
                .build();
    }

    /**
     * ✅ Load authorities từ vai trò của user
     */
    private Collection<? extends GrantedAuthority> getAuthorities(NguoiDung nguoiDung) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // ✅ Thêm vai trò như authorities
        for (VaiTro vaiTro : nguoiDung.getVaiTroSet()) {
            String roleName = vaiTro.getTenVaiTro();
            log.debug("➕ Adding role authority: [{}]", roleName);
            authorities.add(new SimpleGrantedAuthority(roleName));

            // ✅ Thêm quyền từ vai trò (nếu có relationship với bảng Quyen)
            if (vaiTro.getQuyenSet() != null) {
                vaiTro.getQuyenSet().forEach(quyen -> {
                    String quyenName = quyen.getTenQuyen();
                    log.debug("➕ Adding permission authority: [{}]", quyenName);
                    authorities.add(new SimpleGrantedAuthority(quyenName));
                });
            }
        }

        // ✅ FALLBACK: Nếu user không có role nào, cho default permissions
        if (authorities.isEmpty()) {
            log.warn("⚠️ User [{}] has no roles, adding default permissions", nguoiDung.getTenDangNhap());

            // Kiểm tra nếu là admin username thì cho quyền admin
            if ("admin".equals(nguoiDung.getTenDangNhap())) {
                log.info("🔑 Adding ADMIN authorities for user [admin]");
                authorities.add(new SimpleGrantedAuthority("QUAN_TRI_VIEN"));
                authorities.add(new SimpleGrantedAuthority("QUAN_LY_NGUOI_DUNG_XEM"));
                authorities.add(new SimpleGrantedAuthority("QUAN_LY_NGUOI_DUNG_THEM"));
                authorities.add(new SimpleGrantedAuthority("QUAN_LY_NGUOI_DUNG_SUA"));
                authorities.add(new SimpleGrantedAuthority("QUAN_LY_NGUOI_DUNG_XOA"));
            } else {
                // Default permissions cho user thường
                authorities.add(new SimpleGrantedAuthority("USER"));
            }
        }

        return authorities;
    }
}
