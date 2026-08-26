package com.hcmute.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.hcmute.demo.util.Constant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/image")
public class DownloadImageController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        String fileName =
                req.getParameter("fname");

        if (fileName == null || fileName.isBlank()) {

            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Tên file không hợp lệ.");
            return;
        }

        Path uploadDir =Path.of(Constant.DIR).toAbsolutePath().normalize();
        Path file =uploadDir.resolve(fileName).normalize();

    
        if (!file.startsWith(uploadDir)) {

            resp.sendError( HttpServletResponse.SC_FORBIDDEN,"Đường dẫn file không hợp lệ.");
            return;
        }

        if (!Files.exists(file)|| !Files.isRegularFile(file)) { resp.sendError(HttpServletResponse.SC_NOT_FOUND,"Không tìm thấy hình ảnh." );
            return;
        }

        String contentType =getServletContext().getMimeType(file.getFileName().toString());
        resp.setContentType(contentType != null? contentType: "application/octet-stream");

        Files.copy(file,resp.getOutputStream());
    }
}