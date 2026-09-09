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

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AccountService accountService = new AccountServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/account/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");

        req.setAttribute("username", username); 
        if (isBlank(username) || isBlank(password)) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            req.getRequestDispatcher("/views/account/login.jsp").forward(req, resp);
            return;
        }

        Result result = accountService.login(username, password);

        switch (result) {
            case NOT_FOUND:
            case WRONG_PASSWORD:
                req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
                req.getRequestDispatcher("/views/account/login.jsp").forward(req, resp);
                return;

            case NOT_ACTIVE:
                req.setAttribute("error", "Tài khoản chưa được kích hoạt. Vui lòng xác thực OTP qua email.");
                req.getRequestDispatcher("/views/account/login.jsp").forward(req, resp);
                return;

            case OK:
            default:
                Account account = accountService.getByUsername(username);
                HttpSession session = req.getSession();
                session.setAttribute("account", account);
                resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    private static String trim(String v) { return v == null ? null : v.trim(); }
    private static boolean isBlank(String v) { return v == null || v.trim().isEmpty(); }
}