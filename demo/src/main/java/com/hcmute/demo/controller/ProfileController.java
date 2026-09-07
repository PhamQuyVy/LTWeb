package com.hcmute.demo.controller;

import java.io.IOException;

import com.hcmute.demo.entity.Account;
import com.hcmute.demo.entity.User;
import com.hcmute.demo.service.UserProfileService;
import com.hcmute.demo.service.impl.UserProfileServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@MultipartConfig(
        fileSizeThreshold = 1024 * 100,
        maxFileSize = 2 * 1024 * 1024,
        maxRequestSize = 3 * 1024 * 1024
)
@WebServlet("/profile")
public class ProfileController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserProfileService profileService =
            new UserProfileServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        Account account = session == null
                ? null
                : (Account) session.getAttribute("account");

        // Chưa đăng nhập
        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user =
                profileService.getProfile(account.getId());

        if (user == null) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("user", user);

        req.getRequestDispatcher(
                "/views/account/profile.jsp"
        ).forward(req, resp);
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);

        Account account = session == null
                ? null
                : (Account) session.getAttribute("account");

        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String fullName =
                trim(req.getParameter("fullName"));

        String phone =
                trim(req.getParameter("phone"));

        // Validate fullname
        if (fullName == null || fullName.isBlank()) {
            showError(
                    req,
                    resp,
                    account.getId(),
                    "Họ và tên không được để trống."
            );
            return;
        }

        if (fullName.length() > 100) {
            showError(
                    req,
                    resp,
                    account.getId(),
                    "Họ và tên tối đa 100 ký tự."
            );
            return;
        }

        // Validate phone
        if (phone != null
                && !phone.isBlank()
                && !phone.matches(
                    "^(0|\\+84)[0-9]{9,10}$")) {

            showError(
                    req,
                    resp,
                    account.getId(),
                    "Số điện thoại không hợp lệ."
            );
            return;
        }

        try {

            // Lấy file từ multipart
            Part avatarPart =
                    req.getPart("avatar");

            profileService.updateProfile(
                    account.getId(),
                    fullName,
                    phone,
                    avatarPart
            );

            // Cập nhật tên trong session
            account.setFullName(fullName);
            session.setAttribute("account", account);

            resp.sendRedirect(
                    req.getContextPath()
                    + "/profile?success=1"
            );

        } catch (IllegalArgumentException e) {

            showError(
                    req,
                    resp,
                    account.getId(),
                    e.getMessage()
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    req,
                    resp,
                    account.getId(),
                    "Không thể cập nhật hồ sơ. Vui lòng thử lại."
            );
        }
    }

    private void showError(
            HttpServletRequest req,
            HttpServletResponse resp,
            int userId,
            String message)
            throws ServletException, IOException {

        User user =
                profileService.getProfile(userId);

        req.setAttribute("user", user);
        req.setAttribute("error", message);

        req.getRequestDispatcher(
                "/views/account/profile.jsp"
        ).forward(req, resp);
    }

    private static String trim(String value) {
        return value == null
                ? null
                : value.trim();
    }
}