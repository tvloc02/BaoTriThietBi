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

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final NguoiDungRepository nguoiDungRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("🔍 === LOADING USER DETAILS ===");
        log.info("👤 Username: {}", username);

        Optional<NguoiDung> userOpt = nguoiDungRepository.findByTenDangNhap(username);

        if (userOpt.isEmpty()) {
            log.error("❌ User not found: {}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        NguoiDung user = userOpt.get();

        // ✅ DEBUG: Kiểm tra user info
        log.info("✅ User found: {}", user.getTenDangNhap());
        log.info("📧 Email: {}", user.getEmail());
        log.info("👤 Full name: {}", user.getHoVaTen());
        log.info("🔐 Password hash: {}", user.getMatKhau().substring(0, 20) + "...");

        // ✅ DEBUG: Kiểm tra account status
        log.info("🟢 Account enabled: {}", user.getTrangThaiHoatDong());
        log.info("🔓 Account not locked: {}", user.getTaiKhoanKhongBiKhoa());
        log.info("📅 Account not expired: {}", user.getTaiKhoanKhongHetHan());
        log.info("✅ Credentials not expired: {}", user.getThongTinDangNhapHopLe());

        // ✅ DEBUG: Kiểm tra roles từ database
        log.info("🎭 Raw roles from DB: {}", user.getVaiTroSet());
        log.info("📊 Total roles count: {}", user.getVaiTroSet().size());

        // ✅ DEBUG: Log từng role một cách chi tiết
        for (VaiTro vaiTro : user.getVaiTroSet()) {
            log.info("📋 Role Detail - ID: {}, Name: [{}], Active: {}",
                    vaiTro.getIdVaiTro(),
                    vaiTro.getTenVaiTro(),
                    vaiTro.getTrangThaiHoatDong());
        }

        // ✅ Convert roles to authorities
        Collection<GrantedAuthority> authorities = user.getVaiTroSet().stream()
                .filter(vaiTro -> vaiTro.getTrangThaiHoatDong()) // Chỉ lấy role active
                .map(vaiTro -> {
                    String roleName = vaiTro.getTenVaiTro();
                    log.info("🔄 Converting role: [{}] -> GrantedAuthority", roleName);
                    return new SimpleGrantedAuthority(roleName);
                })
                .collect(Collectors.toList());

        // ✅ DEBUG: Final authorities
        log.info("✅ Final authorities count: {}", authorities.size());
        log.info("✅ Final authorities list: {}", authorities);

        // ✅ Tạo UserDetails object
        UserDetails userDetails = User.builder()
                .username(user.getTenDangNhap())
                .password(user.getMatKhau())
                .authorities(authorities)
                .accountExpired(!user.getTaiKhoanKhongHetHan())
                .accountLocked(!user.getTaiKhoanKhongBiKhoa())
                .credentialsExpired(!user.getThongTinDangNhapHopLe())
                .disabled(!user.getTrangThaiHoatDong())
                .build();

        log.info("🎯 UserDetails created successfully for: {}", username);
        log.info("🎭 UserDetails authorities: {}", userDetails.getAuthorities());

        return userDetails;
    }

    /**
     * ✅ Helper method để kiểm tra user có role cụ thể không
     */
    public boolean hasRole(String username, String roleName) {
        try {
            UserDetails userDetails = loadUserByUsername(username);
            return userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals(roleName));
        } catch (Exception e) {
            log.error("❌ Error checking role for user {}: {}", username, e.getMessage());
            return false;
        }
    }

    /**
     * ✅ Helper method để lấy tất cả roles của user
     */
    public Collection<String> getUserRoles(String username) {
        try {
            UserDetails userDetails = loadUserByUsername(username);
            return userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("❌ Error getting roles for user {}: {}", username, e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
}