package com.hethong.baotri.dto.nguoi_dung;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungDTO {

    private Long idNguoiDung;

    // Thêm alias getId()
    public Long getId() {
        return getIdNguoiDung();
    }

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3-50 ký tự")
    private String tenDangNhap;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String matKhau;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 100, message = "Họ và tên không được vượt quá 100 ký tự")
    private String hoVaTen;

    @Email(message = "Email không đúng định dạng")
    private String email;

    @Size(max = 15, message = "Số điện thoại không được vượt quá 15 ký tự")
    private String soDienThoai;

    @Size(max = 200, message = "Địa chỉ không được vượt quá 200 ký tự")
    private String diaChi;

    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    private LocalDateTime lanDangNhapCuoi;
    private Boolean trangThaiHoatDong;
    private Boolean taiKhoanKhongBiKhoa;
    private Integer soLanDangNhapThatBai;
    private Set<VaiTroDTO> vaiTroSet;
    private String tenDoiBaoTri;
}