package com.harbin.mymallcart.service;

import com.harbin.mymallcart.vo.Cart;
import com.harbin.mymallcart.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Yuanzz
 * @creat 2021-03-07-16:43
 */
public interface CartService {
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItem getCartItem(Long skuId);

    Cart getCart() throws ExecutionException, InterruptedException;

    void checkItem(Long skuId, Integer check);

    void changeItemCount(Long skuId, Integer num);

    void deleteItem(Long skuId);

    List<CartItem> getUserCartItems();
}
