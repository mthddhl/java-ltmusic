package com.cumt.musicpay.service;

import com.cumt.musicpay.domain.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.musicpay.util.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mthddhl
 * @since 2022-08-07
 */
public interface IProductService extends IService<Product> {

    Result addVIPProduct(Product product);

    Result getAllVIPProduct();

    Result getAllProduct();

}
