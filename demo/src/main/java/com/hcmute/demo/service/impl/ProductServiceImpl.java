package com.hcmute.demo.service.impl;

import java.util.List;

import com.hcmute.demo.dao.IProductDao;
import com.hcmute.demo.dao.impl.ProductDao;
import com.hcmute.demo.entity.Product;
import com.hcmute.demo.service.IProductService;

public class ProductServiceImpl implements IProductService {

    private final IProductDao productDao = new ProductDao();

    @Override
    public void insert(Product product) {
        productDao.insert(product);
    }

    @Override
    public void update(Product product) {
        productDao.update(product);
    }

    @Override
    public void delete(int id) {
        try {
            productDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product findById(int id) {
        return productDao.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public List<Product> findAll(int page, int pagesize) {
        return productDao.findAll(page, pagesize);
    }

    @Override
    public List<Product> findNewest(int limit) {
        return productDao.findNewest(limit);
    }

    @Override
    public int count() {
        return productDao.count();
    }
}