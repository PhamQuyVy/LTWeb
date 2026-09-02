package com.hcmute.demo.controller;

import java.io.IOException;

import com.hcmute.demo.entity.Account;
import com.hcmute.demo.service.AccountService;
import com.hcmute.demo.service.AccountService.Result;
import com.hcmute.demo.service.impl.AccountServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AccountService accountService = new AccountServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/views/account/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String fullName = trim(req.getParameter("fullName"));
        String email = trim(req.getParameter("email"));
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");

        if (isBlank(fullName) || isBlank(email) || isBlank(username) || isBlank(password)) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            forwardBack(req, resp);
            return;
        }

        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            req.setAttribute("error", "Email không hợp lệ.");
            forwardBack(req, resp);
            return;
        }

        Account account = new Account();
        account.setFullName(fullName);
        account.setEmail(email);
        account.setUsername(username);
        account.setPassword(password);

        Result result = accountService.register(account);

        switch (result) {

            case EMAIL_EXISTED:
                req.setAttribute("error", "Email này đã được đăng ký.");
                forwardBack(req, resp);
                return;

            case USERNAME_EXISTED:
                req.setAttribute("error", "Tên đăng nhập đã tồn tại.");
                forwardBack(req, resp);
                return;

            case MAIL_ERROR:
                req.setAttribute("error", "Không thể gửi email OTP. Vui lòng thử lại sau.");
                forwardBack(req, resp);
                return;

            case OK:
            default:
                HttpSession session = req.getSession();
                session.setAttribute("pendingEmail", email);
                resp.sendRedirect(req.getContextPath() + "/verify-otp");
        }
    }

    private void forwardBack(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/account/register.jsp").forward(req, resp);
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}