package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: ShoppingCartServiceImpl
 * Package: com.sky.service.impl
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/7 下午3:37
 * @Version 1.0
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 1. 准备工作
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId(); // 获取当前微信用户的id
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart); // 查询购物车记录

        // 2. 判断当前加入到购物车中的商品是否已经存在
        if (list != null && !list.isEmpty()) {
            // 2.1 如果存在，则数量增加
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        } else {
            // 2.2 如果不存在，需要插入一条购物车记录
            // 判断本次添加到购物车的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                // 2.2.1 如果本次添加到购物车的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName())
                        .setImage(dish.getImage())
                        .setAmount(dish.getPrice())
                        .setNumber(1)
                        .setCreateTime(LocalDateTime.now());
            } else {
                // 2.2.2 如果本次添加到购物车的是套餐
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName())
                        .setImage(setmeal.getImage())
                        .setAmount(setmeal.getPrice())
                        .setNumber(1)
                        .setCreateTime(LocalDateTime.now());
            }
            // 插入购物车记录
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        // 1. 获取当前微信用户的id
        Long userId = BaseContext.getCurrentId();
        // 2. 查询购物车记录
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(userId).build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        return shoppingCartList;
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanShoppingCart() {
        // 1. 获取当前微信用户的id
        Long userId = BaseContext.getCurrentId();
        // 2. 清空购物车记录
        shoppingCartMapper.deleteByUserId(userId);
    }

    /**
     * 删除购物车中一个商品
     *
     * @param shoppingCartDTO
     */
    @Transactional // 涉及多个sql语句，需要事务
    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .dishId(shoppingCartDTO.getDishId())
                .setmealId(shoppingCartDTO.getSetmealId())
                .dishFlavor(shoppingCartDTO.getDishFlavor()).build();
        // 查询购物车记录，且记录唯一
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list == null || list.isEmpty()) {
            return;
        }
        ShoppingCart singleShoppingCart = list.get(0);
        Integer number = singleShoppingCart.getNumber();

        // number==1时，直接删除该购物车记录
        if (number == 1){
            shoppingCartMapper.deleteById(singleShoppingCart.getId());
        }else if (number > 1) {
            // number>1时，数量减1
            singleShoppingCart.setNumber(number - 1);
            shoppingCartMapper.updateNumberById(singleShoppingCart);
        }
    }
}
