package com.hcmute.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

@MultipartConfig()
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
        String url = req.getRequestURI();

        if (url.contains("/admin/product/insert")) {
            String productname = req.getParameter("productname");
            double price = Double.parseDouble(req.getParameter("price"));
            String description = req.getParameter("description");
            int status = Integer.parseInt(req.getParameter("status"));
            int cateId = Integer.parseInt(req.getParameter("cateid"));
            String images = req.getParameter("images");

            Product product = new Product();
            product.setProductName(productname);
            product.setPrice(price);
            product.setDescription(description);
            product.setStatus(status);
            product.setCategory(cateService.findById(cateId));

            String fname = uploadImage(req, "avatar.png");
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

        if (url.contains("/admin/product/update")) {
            int productId = Integer.parseInt(req.getParameter("productid"));
            String productname = req.getParameter("productname");
            double price = Double.parseDouble(req.getParameter("price"));
            String description = req.getParameter("description");
            int status = Integer.parseInt(req.getParameter("status"));
            int cateId = Integer.parseInt(req.getParameter("cateid"));
            String images = req.getParameter("images");

            Product product = productService.findById(productId);
            String fileold = product.getImages();
            product.setProductName(productname);
            product.setPrice(price);
            product.setDescription(description);
            product.setStatus(status);
            product.setCategory(cateService.findById(cateId));

            String fname = uploadImage(req, fileold);
            if (fname != null && !fname.equals(fileold)) {
                if (fileold != null && fileold.length() >= 5 && !fileold.substring(0, 5).equals("https")) {
                    deleteFile(Constant.DIR + File.separator + fileold);
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
    }

    private String uploadImage(HttpServletRequest req, String fallback) {
        String uploadPath = Constant.DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        try {
            Part part = req.getPart("images1");
            if (part != null && part.getSize() > 0) {
                String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                int index = filename.lastIndexOf(".");
                String ext = filename.substring(index + 1);
                String fname = System.currentTimeMillis() + "." + ext;
                part.write(uploadPath + "/" + fname);
                return fname;
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }
}