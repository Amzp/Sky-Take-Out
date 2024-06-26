package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: DishService
 * Package: com.sky.service
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/4 9:57
 * @Version 1.0
 */

public interface DishService {

    /**
     * 新增菜品及其口味
     * @param dishDTO
     */
    void addDishWithFlavors(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQueryDish(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据ids批量删除菜品
     * @param ids
     */
    void deleteDishBatch(List<Long> ids);

    /**
     * 根据id查询菜品及其口味
     * @param id
     * @return
     */
    DishVO queryDishByIdWithFlavors(Long id);

    /**
     * 更新菜品及其口味
     * @param dishDTO
     */
    void updateDishWithFlavors(DishDTO dishDTO);

    /**
     * 根据分类id查询菜品及其口味
     * @param categoryId
     * @return
     */
    List<Dish> queryDishByCategoryId(Long categoryId);

    /**
     * 根据id更新菜品销售状态
     * @param status
     * @param id
     */
    void updateSaleStatus(Integer status, Long id);

    /**
     * C端-根据分类id查询菜品（条件查询菜品和口味）
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
