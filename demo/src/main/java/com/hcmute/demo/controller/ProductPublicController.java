package com.hcmute.demo.controller;

import java.io.IOException;
import java.util.List;

import com.hcmute.demo.entity.Product;
import com.hcmute.demo.service.IProductService;
import com.hcmute.demo.service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/product", "/product/detail" })
public class ProductPublicController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6;
    private final IProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();

        if (url.contains("/product/detail")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Product product = productService.findById(id);
            req.setAttribute("product", product);
            req.getRequestDispatcher("/views/product/detail.jsp").forward(req, resp);
            return;
        }

        int page = 0;
        try {
            page = Integer.parseInt(req.getParameter("page"));
        } catch (Exception ignored) { }
        if (page < 0) page = 0;

        int total = productService.count();
        int totalPages = (int) Math.ceil(total / (double) PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;
        if (page >= totalPages) page = totalPages - 1;

        List<Product> list = productService.findAll(page, PAGE_SIZE);

        req.setAttribute("listproduct", list);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/views/product/list.jsp").forward(req, resp);
    }
}