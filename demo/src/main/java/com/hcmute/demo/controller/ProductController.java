package com.hcmute.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.hcmute.demo.entity.Category;
import com.hcmute.demo.entity.Product;
import com.hcmute.demo.service.ICategoryService;
import com.hcmute.demo.service.IProductService;
import com.hcmute.demo.service.impl.CategoryServiceImpl;
import com.hcmute.demo.service.impl.ProductServiceImpl;
import com.hcmute.demo.util.Constant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
@WebServlet(urlPatterns = { "/admin/products", "/admin/product/add", "/admin/product/insert",
        "/admin/product/edit", "/admin/product/update", "/admin/product/delete" })
public class ProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final IProductService productService = new ProductServiceImpl();
    private final ICategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();

        if (url.contains("/admin/products")) {
            List<Product> list = productService.findAll();
            req.setAttribute("listproduct", list);
            req.getRequestDispatcher("/views/admin/product-list.jsp").forward(req, resp);

        } else if (url.contains("/admin/product/add")) {
            req.setAttribute("listcate", cateService.findAll());
            req.getRequestDispatcher("/views/admin/product-add.jsp").forward(req, resp);

        } else if (url.contains("/admin/product/edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Product product = productService.findById(id);
            req.setAttribute("product", product);
            req.setAttribute("listcate", cateService.findAll());
            req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);

        } else {
            int id = Integer.parseInt(req.getParameter("id"));
            productService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        if (url.contains("/admin/product/insert")) {
            handleInsert(req, resp);
        } else if (url.contains("/admin/product/update")) {
            handleUpdate(req, resp);
        }
    }

    private void handleInsert(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String productname = trim(req.getParameter("productname"));
        String priceStr = trim(req.getParameter("price"));
        String description = trim(req.getParameter("description"));
        String statusStr = req.getParameter("status");
        String cateIdStr = req.getParameter("cateid");
        String images = trim(req.getParameter("images"));

        String error = validate(productname, priceStr, cateIdStr);
        if (error == null) {
            Part avatarPart = getPartSafely(req, "images1");
            error = validateImagePart(avatarPart);
        }

        if (error != null) {
            forwardAddWithError(req, resp, error);
            return;
        }

        double price = Double.parseDouble(priceStr);
        int status = statusStr == null ? 1 : Integer.parseInt(statusStr);
        int cateId = Integer.parseInt(cateIdStr);
        Category category = cateService.findById(cateId);
        if (category == null) {
            forwardAddWithError(req, resp, "Danh mục không tồn tại.");
            return;
        }

        Product product = new Product();
        product.setProductName(productname);
        product.setPrice(price);
        product.setDescription(description);
        product.setStatus(status);
        product.setCategory(category);

        String fname = uploadImage(req);
        if (fname != null) {
            product.setImages(fname);
        } else if (images != null && !images.isEmpty()) {
            product.setImages(images);
        } else {
            product.setImages("avatar.png");
        }

        productService.insert(product);
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int productId = Integer.parseInt(req.getParameter("productid"));
        Product product = productService.findById(productId);
        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/products");
            return;
        }

        String productname = trim(req.getParameter("productname"));
        String priceStr = trim(req.getParameter("price"));
        String description = trim(req.getParameter("description"));
        String statusStr = req.getParameter("status");
        String cateIdStr = req.getParameter("cateid");
        String images = trim(req.getParameter("images"));

        String error = validate(productname, priceStr, cateIdStr);
        if (error == null) {
            Part avatarPart = getPartSafely(req, "images1");
            error = validateImagePart(avatarPart);
        }

        if (error != null) {
            forwardEditWithError(req, resp, product, error);
            return;
        }

        double price = Double.parseDouble(priceStr);
        int status = statusStr == null ? product.getStatus() : Integer.parseInt(statusStr);
        int cateId = Integer.parseInt(cateIdStr);
        Category category = cateService.findById(cateId);
        if (category == null) {
            forwardEditWithError(req, resp, product, "Danh mục không tồn tại.");
            return;
        }

        String fileold = product.getImages();
        product.setProductName(productname);
        product.setPrice(price);
        product.setDescription(description);
        product.setStatus(status);
        product.setCategory(category);

        String fname = uploadImage(req);
        if (fname != null) {
            if (fileold != null && fileold.length() >= 5 && !fileold.substring(0, 5).equals("https")) {
                deleteFileQuietly(Constant.DIR + File.separator + fileold);
            }
            product.setImages(fname);
        } else if (images != null && !images.isEmpty()) {
            product.setImages(images);
        } else {
            product.setImages(fileold);
        }

        productService.update(product);
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

    /** Validate các trường bắt buộc, trả về thông báo lỗi hoặc null nếu hợp lệ. */
    private String validate(String productname, String priceStr, String cateIdStr) {
        if (productname == null || productname.isEmpty()) {
            return "Tên sản phẩm không được để trống.";
        }
        if (productname.length() > 150) {
            return "Tên sản phẩm tối đa 150 ký tự.";
        }
        if (priceStr == null || priceStr.isEmpty()) {
            return "Vui lòng nhập giá sản phẩm.";
        }
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            return "Giá sản phẩm phải là số.";
        }
        if (price <= 0) {
            return "Giá sản phẩm phải lớn hơn 0.";
        }
        if (cateIdStr == null || cateIdStr.isEmpty()) {
            return "Vui lòng chọn danh mục.";
        }
        try {
            Integer.parseInt(cateIdStr);
        } catch (NumberFormatException e) {
            return "Danh mục không hợp lệ.";
        }
        return null;
    }

    private String validateImagePart(Part part) {
        if (part == null || part.getSize() <= 0) return null; // không upload thì bỏ qua
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

    private void forwardAddWithError(HttpServletRequest req, HttpServletResponse resp, String error)
            throws ServletException, IOException {
        req.setAttribute("error", error);
        req.setAttribute("listcate", cateService.findAll());
        req.getRequestDispatcher("/views/admin/product-add.jsp").forward(req, resp);
    }

    private void forwardEditWithError(HttpServletRequest req, HttpServletResponse resp,
                                       Product product, String error)
            throws ServletException, IOException {
        req.setAttribute("error", error);
        req.setAttribute("product", product);
        req.setAttribute("listcate", cateService.findAll());
        req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);
    }

    private String uploadImage(HttpServletRequest req) {
        String uploadPath = Constant.DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        try {
            Part part = req.getPart("images1");
            if (part != null && part.getSize() > 0) {
                String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                int index = filename.lastIndexOf(".");
                String ext = index >= 0 ? filename.substring(index + 1) : "";
                String fname = System.currentTimeMillis() + "." + ext;
                part.write(uploadPath + "/" + fname);
                return fname;
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void deleteFileQuietly(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException ignored) {}
    }

    private static String trim(String v) { return v == null ? null : v.trim(); }
}