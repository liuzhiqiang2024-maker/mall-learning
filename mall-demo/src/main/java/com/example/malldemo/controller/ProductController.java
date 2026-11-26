package com.example.malldemo.controller;

import com.example.malldemo.auth.AuthInterceptor;
import com.example.malldemo.common.Result;
import com.example.malldemo.dto.OrderDetailVO;
import com.example.malldemo.dto.ProductVO;
import com.example.malldemo.entity.Product;
import com.example.malldemo.service.ProductService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Validated
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/getById")
    public Result<ProductVO> getProductById (@RequestParam(required = false)
                                                 @NotNull(message = "商品ID不能为空")
                                                 @Positive(message = "商品ID必须大于0")
                                                 Long productId) {
        Product product= productService.getProductById(productId);
        ProductVO vo=new ProductVO();
        vo.setPrice(product.getPrice());
        vo.setProductName(product.getProductName());
        vo.setId(product.getId());
        return Result.success(vo);
    }

}
