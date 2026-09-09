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

@WebServlet("/reset-password")
public class ResetPasswordController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AccountService accountService = new AccountServiceImpl();
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$";
    private static final String OTP_REGEX = "^\\d{6}$";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }
        req.setAttribute("email", email);
        req.getRequestDispatcher("/views/account/reset-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("resetEmail");

        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        String otpCode = trim(req.getParameter("otpCode"));
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        req.setAttribute("email", email);

        if (isBlank(otpCode) || isBlank(newPassword) || isBlank(confirmPassword)) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            req.getRequestDispatcher("/views/account/reset-password.jsp").forward(req, resp);
            return;
        }

        if (!otpCode.matches(OTP_REGEX)) {
            req.setAttribute("error", "Mã OTP phải gồm đúng 6 chữ số.");
            req.getRequestDispatcher("/views/account/reset-password.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.matches(PASSWORD_REGEX)) {
            req.setAttribute("error", "Mật khẩu tối thiểu 6 ký tự, gồm cả chữ và số.");
            req.getRequestDispatcher("/views/account/reset-password.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            req.getRequestDispatcher("/views/account/reset-password.jsp").forward(req, resp);
            return;
        }

        Result result = accountService.resetPassword(email, otpCode, newPassword);

        switch (result) {
            case OTP_WRONG:
                req.setAttribute("error", "Mã OTP không đúng.");
                req.getRequestDispatcher("/views/account/reset-password.jsp").forward(req, resp);
                return;

            case OTP_EXPIRED:
                req.setAttribute("error", "Mã OTP đã hết hạn. Vui lòng yêu cầu lại.");
                req.getRequestDispatcher("/views/account/reset-password.jsp").forward(req, resp);
                return;

            case NOT_FOUND:
                resp.sendRedirect(req.getContextPath() + "/forgot-password");
                return;

            case OK:
            default:
                session.removeAttribute("resetEmail");
                resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    private static String trim(String v) { return v == null ? null : v.trim(); }
    private static boolean isBlank(String v) { return v == null || v.trim().isEmpty(); }
}