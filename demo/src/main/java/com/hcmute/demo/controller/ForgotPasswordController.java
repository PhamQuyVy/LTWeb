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

@WebServlet("/forgot-password")
public class ForgotPasswordController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AccountService accountService = new AccountServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/account/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");

        Result result = accountService.forgotPassword(email);

        switch (result) {
            case NOT_FOUND:
                req.setAttribute("error", "Email không tồn tại trong hệ thống.");
                req.getRequestDispatcher("/views/account/forgot-password.jsp").forward(req, resp);
                return;

            case MAIL_ERROR:
                req.setAttribute("error", "Không thể gửi email OTP. Vui lòng thử lại sau.");
                req.getRequestDispatcher("/views/account/forgot-password.jsp").forward(req, resp);
                return;

            case OK:
            default:
                HttpSession session = req.getSession();
                session.setAttribute("resetEmail", email);
                resp.sendRedirect(req.getContextPath() + "/reset-password");
        }
    }
}