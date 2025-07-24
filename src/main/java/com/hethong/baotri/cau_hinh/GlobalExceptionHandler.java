package com.hethong.baotri.cau_hinh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            log.error("Error {} occurred: {}", statusCode, message);

            model.addAttribute("statusCode", statusCode);
            model.addAttribute("message", message);

            // ✅ Sử dụng trang lỗi có sẵn thay vì template không tồn tại
            return "error/404";
        }

        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model) {
        log.error("Unexpected error occurred", e);

        model.addAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn");

        // ✅ Sử dụng trang lỗi đơn giản
        return "error/404";
    }
}