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

/**
 * Xem & cập nhật thông tin cá nhân (fullName, phone, avatar).
 * GET  /profile -> hiển thị form với dữ liệu hiện tại
 * POST /profile -> nhận multipart/form-data, cập nhật DB qua JPA
 */
@WebServlet("/profile")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,       // 5MB / file
        maxRequestSize = 10 * 1024 * 1024,   // 10MB / request
        fileSizeThreshold = 1024 * 1024      // > 1MB thì ghi ra file tạm thay vì giữ trong RAM
)
public class ProfileController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserProfileService userProfileService = new UserProfileServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Account account = getLoggedAccount(req);
        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Thông báo flash (nếu vừa POST xong và redirect về đây)
        HttpSession session = req.getSession();
        Object flash = session.getAttribute("profileMessage");
        if (flash != null) {
            req.setAttribute("message", flash);
            session.removeAttribute("profileMessage");
        }

        User user = userProfileService.getProfile(account.getId());
        req.setAttribute("user", user);
        req.getRequestDispatcher("/views/account/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        Account account = getLoggedAccount(req);
        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");

        // Validate cơ bản
        if (fullName == null || fullName.trim().isEmpty()) {
            forwardWithError(req, resp, account, "Họ tên không được để trống.");
            return;
        }
        if (phone != null && !phone.isBlank() && !phone.matches("^[0-9+()\\-\\s]{8,20}$")) {
            forwardWithError(req, resp, account, "Số điện thoại không hợp lệ.");
            return;
        }

        Part avatarPart = req.getPart("avatarFile");
        if (avatarPart != null && avatarPart.getSize() > 0) {
            String contentType = avatarPart.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                forwardWithError(req, resp, account, "File tải lên phải là hình ảnh (jpg, png, gif...).");
                return;
            }
        }

        try {
            userProfileService.updateProfile(account.getId(), fullName.trim(), phone, avatarPart);

            // Đồng bộ lại full name hiển thị trên header (session đang giữ object Account)
            account.setFullName(fullName.trim());
            req.getSession().setAttribute("account", account);

            req.getSession().setAttribute("profileMessage", "Cập nhật thông tin thành công!");
            resp.sendRedirect(req.getContextPath() + "/profile");

        } catch (Exception e) {
            forwardWithError(req, resp, account, "Cập nhật thất bại: " + e.getMessage());
        }
    }

    private void forwardWithError(HttpServletRequest req, HttpServletResponse resp,
                                   Account account, String errorMessage)
            throws ServletException, IOException {
        req.setAttribute("error", errorMessage);
        User user = userProfileService.getProfile(account.getId());
        req.setAttribute("user", user);
        req.getRequestDispatcher("/views/account/profile.jsp").forward(req, resp);
    }

    private Account getLoggedAccount(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (Account) session.getAttribute("account");
    }
}
