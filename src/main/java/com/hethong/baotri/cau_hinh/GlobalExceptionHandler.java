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
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            log.error("Error {} occurred: {}", statusCode, message);

            model.addAttribute("statusCode", statusCode);
            model.addAttribute("message", message);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("title", "Trang không tìm thấy - 404");
                model.addAttribute("errorTitle", "Oops! Trang không tồn tại");
                model.addAttribute("errorMessage", "Trang bạn đang tìm kiếm không tồn tại hoặc đã bị di chuyển.");
                return "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("title", "Lỗi hệ thống - 500");
                model.addAttribute("errorTitle", "Lỗi hệ thống");
                model.addAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.");
                return "error/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("title", "Không có quyền truy cập - 403");
                model.addAttribute("errorTitle", "Không có quyền truy cập");
                model.addAttribute("errorMessage", "Bạn không có quyền truy cập vào trang này.");
                return "error/403";
            }
        }

        model.addAttribute("title", "Lỗi hệ thống");
        model.addAttribute("errorTitle", "Đã xảy ra lỗi");
        model.addAttribute("errorMessage", "Vui lòng thử lại sau hoặc liên hệ quản trị viên.");
        return "error/general";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model) {
        log.error("Unexpected error occurred", e);

        model.addAttribute("title", "Lỗi hệ thống");
        model.addAttribute("errorTitle", "Đã xảy ra lỗi không mong muốn");
        model.addAttribute("errorMessage", "Vui lòng thử lại sau hoặc liên hệ quản trị viên.");
        model.addAttribute("exception", e.getMessage());

        return "error/general";
    }
}