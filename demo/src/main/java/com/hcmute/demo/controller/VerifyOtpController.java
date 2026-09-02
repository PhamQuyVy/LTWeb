package com.hcmute.demo.controller;

import java.io.IOException;

import com.hcmute.demo.service.AccountService;
import com.hcmute.demo.service.AccountService.Result;
import com.hcmute.demo.service.impl.AccountServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/verify-otp")
public class VerifyOtpController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AccountService accountService = new AccountServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("pendingEmail");

        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        req.setAttribute("email", email);
        req.getRequestDispatcher("/views/account/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("pendingEmail");

        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        String action = req.getParameter("action");

        if ("resend".equals(action)) {

            Result resendResult = accountService.resendOtp(email);

            if (resendResult == Result.MAIL_ERROR) {
                req.setAttribute("error", "Không thể gửi lại email OTP. Vui lòng thử lại.");
            } else {
                req.setAttribute("message", "Mã OTP mới đã được gửi tới email của bạn.");
            }

            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/account/verify-otp.jsp").forward(req, resp);
            return;
        }

        String otpCode = trim(req.getParameter("otpCode"));

        if (otpCode == null || otpCode.isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập mã OTP.");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/account/verify-otp.jsp").forward(req, resp);
            return;
        }

        Result result = accountService.verifyOtp(email, otpCode);

        switch (result) {

            case OK:
                session.removeAttribute("pendingEmail");
                req.setAttribute("success", true);
                req.getRequestDispatcher("/views/account/verify-success.jsp").forward(req, resp);
                return;

            case OTP_WRONG:
                req.setAttribute("error", "Mã OTP không đúng.");
                break;

            case OTP_EXPIRED:
                req.setAttribute("error", "Mã OTP đã hết hạn. Vui lòng bấm \"Gửi lại mã\".");
                break;

            case ALREADY_ACTIVE:
                req.setAttribute("error", "Tài khoản đã được kích hoạt trước đó.");
                break;

            case NOT_FOUND:
            default:
                req.setAttribute("error", "Không tìm thấy tài khoản.");
        }

        req.setAttribute("email", email);
        req.getRequestDispatcher("/views/account/verify-otp.jsp").forward(req, resp);
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }
}