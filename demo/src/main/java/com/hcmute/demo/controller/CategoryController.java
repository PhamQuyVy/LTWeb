package com.hcmute.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.hcmute.demo.entity.Category;
import com.hcmute.demo.service.ICategoryService;
import com.hcmute.demo.service.impl.CategoryServiceImpl;
import com.hcmute.demo.util.Constant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
@WebServlet(urlPatterns = { "/admin/categories", "/admin/category/add", "/admin/category/insert",
        "/admin/category/edit", "/admin/category/update", "/admin/category/delete" })
public class CategoryController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public ICategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();

        if (url.contains("/admin/categories")) {
            List<Category> list = cateService.findAll();
            req.setAttribute("listcate", list);
            req.getRequestDispatcher("/views/admin/category-list.jsp").forward(req, resp);

        } else if (url.contains("/admin/category/add")) {
            req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);

        } else if (url.contains("/admin/category/edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Category category = cateService.findById(id);
            req.setAttribute("cate", category);
            req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);

        } else {
            int id = Integer.parseInt(req.getParameter("id"));
            try {
                cateService.delete(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        if (url.contains("/admin/category/insert")) {
            handleInsert(req, resp);
        } else if (url.contains("/admin/category/update")) {
            handleUpdate(req, resp);
        }
    }

    private void handleInsert(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String categoryname = trim(req.getParameter("categoryname"));
        String statusStr = req.getParameter("status");
        String images = trim(req.getParameter("images"));

        String error = validate(categoryname);
        Part part = getPartSafely(req, "images1");
        if (error == null) error = validateImagePart(part);

        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);
            return;
        }

        Category category = new Category();
        category.setCategoryname(categoryname);
        category.setStatus(statusStr == null ? 1 : Integer.parseInt(statusStr));

        String uploadPath = Constant.DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        if (part != null && part.getSize() > 0) {
            category.setImages(saveFile(part, uploadPath));
        } else if (images != null && !images.isEmpty()) {
            category.setImages(images);
        } else {
            category.setImages("avatar.png");
        }

        cateService.insert(category);
        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int categoryid = Integer.parseInt(req.getParameter("categoryid"));
        Category category = cateService.findById(categoryid);
        if (category == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
            return;
        }

        String categoryname = trim(req.getParameter("categoryname"));
        String statusStr = req.getParameter("status");
        String images = trim(req.getParameter("images"));

        String error = validate(categoryname);
        Part part = getPartSafely(req, "images1");
        if (error == null) error = validateImagePart(part);

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("cate", category);
            req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
            return;
        }

        String fileold = category.getImages();
        category.setCategoryname(categoryname);
        category.setStatus(statusStr == null ? category.getStatus() : Integer.parseInt(statusStr));

        String uploadPath = Constant.DIR;

        if (part != null && part.getSize() > 0) {
            if (fileold != null && fileold.length() >= 5 && !fileold.substring(0, 5).equals("https")) {
                deleteFileQuietly(uploadPath + File.separator + fileold);
            }
            category.setImages(saveFile(part, uploadPath));
        } else if (images != null && !images.isEmpty()) {
            category.setImages(images);
        } else {
            category.setImages(fileold);
        }

        cateService.update(category);
        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }

    private String validate(String categoryname) {
        if (categoryname == null || categoryname.isEmpty()) {
            return "Tên danh mục không được để trống.";
        }
        if (categoryname.length() > 100) {
            return "Tên danh mục tối đa 100 ký tự.";
        }
        return null;
    }

    private String validateImagePart(Part part) {
        if (part == null || part.getSize() <= 0) return null;
        String contentType = part.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return "File tải lên phải là hình ảnh (jpg, png, gif...).";
        }
        if (part.getSize() > 5 * 1024 * 1024) {
            return "Kích thước ảnh tối đa 5MB.";
        }
        return null;
    }

    private Part getPartSafely(HttpServletRequest req, String name) {
        try {
            return req.getPart(name);
        } catch (Exception e) {
            return null;
        }
    }

    private String saveFile(Part part, String uploadPath) {
        try {
            String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            int index = filename.lastIndexOf(".");
            String ext = index >= 0 ? filename.substring(index + 1) : "";
            String fname = System.currentTimeMillis() + "." + ext;
            part.write(uploadPath + "/" + fname);
            return fname;
        } catch (IOException e) {
            e.printStackTrace();
            return "avatar.png";
        }
    }

    private static void deleteFileQuietly(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException ignored) {}
    }

    private static String trim(String v) { return v == null ? null : v.trim(); }
}