package com.hethong.baotri.cau_hinh;

import com.hethong.baotri.ngoai_le.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NgoaiLeBaoTri.class)
    public ResponseEntity<Map<String, Object>> handleNgoaiLeBaoTri(NgoaiLeBaoTri e) {
        log.error("Lỗi bảo trì: {}", e.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Lỗi bảo trì", e.getMessage());
    }

    @ExceptionHandler(NgoaiLeThietBi.class)
    public ResponseEntity<Map<String, Object>> handleNgoaiLeThietBi(NgoaiLeThietBi e) {
        log.error("Lỗi thiết bị: {}", e.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Lỗi thiết bị", e.getMessage());
    }

    @ExceptionHandler(NgoaiLeVatTu.class)
    public ResponseEntity<Map<String, Object>> handleNgoaiLeVatTu(NgoaiLeVatTu e) {
        log.error("Lỗi vật tư: {}", e.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Lỗi vật tư", e.getMessage());
    }

    @ExceptionHandler(NgoaiLeNguoiDung.class)
    public ResponseEntity<Map<String, Object>> handleNgoaiLeNguoiDung(NgoaiLeNguoiDung e) {
        log.error("Lỗi người dùng: {}", e.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Lỗi người dùng", e.getMessage());
    }

    @ExceptionHandler(NgoaiLeDoiBaoTri.class)
    public ResponseEntity<Map<String, Object>> handleNgoaiLeDoiBaoTri(NgoaiLeDoiBaoTri e) {
        log.error("Lỗi đội bảo trì: {}", e.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Lỗi đội bảo trì", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("message", "Dữ liệu đầu vào không hợp lệ");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException e) {
        log.error("Từ chối truy cập: {}", e.getMessage());
        return createErrorResponse(HttpStatus.FORBIDDEN, "Từ chối truy cập", "Bạn không có quyền thực hiện chức năng này");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        log.error("Lỗi không xác định: {}", e.getMessage(), e);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", "Đã xảy ra lỗi không xác định");
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}