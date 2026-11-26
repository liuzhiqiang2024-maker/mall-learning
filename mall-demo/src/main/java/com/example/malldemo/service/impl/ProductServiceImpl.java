package com.example.malldemo.service.impl;

import com.example.malldemo.entity.Product;
import com.example.malldemo.exception.BusinessException;
import com.example.malldemo.mapper.ProductMapper;
import com.example.malldemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    public Product getProductById(Long productId) {
        Product product= productMapper.findById(productId);
        if(product==null){
            throw new BusinessException(404,"商品不存在");
        }
        return product;
    }
}
