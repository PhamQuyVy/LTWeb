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

        String otpCode = req.getParameter("otpCode");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        req.setAttribute("email", email);

        if (newPassword == null || !newPassword.equals(confirmPassword)) {
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
}