package com.hcmute.demo.dao;

import java.util.List;

import com.hcmute.demo.entity.Product;

public interface IProductDao {

    void insert(Product product);
    void update(Product product);
    void delete(int id) throws Exception;
    Product findById(int id);
    List<Product> findAll();
    List<Product> findAll(int page, int pagesize);
    List<Product> findNewest(int limit);
    int count();
}