package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName: SetmealServiceImpl
 * Package: com.sky.service.impl
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/4 20:19
 * @Version 1.0
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 保存新增的套餐信息
     *
     * @param setmealDTO
     */
    @Transactional // 开启事务，涉及到两个表（setmeal表和setmeal_dish表），需要事务保持一致性
    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal); // 将setmealDTO中的属性值拷贝到setmeal对象中

        // 1. 向套餐表setmeal中插入1条数据
        setmealMapper.insertSetmeal(setmeal);

        // 2. 获取到刚插入的套餐的id
        Long setmealId = setmeal.getId();

        // 3. 向套餐菜品表setmeal_dish中插入多条数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            // 遍历setmealDishes集合，为每一个套餐菜品对象设置setmealId属性
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
            // 批量插入套餐菜品表
            setmealDishMapper.insertSetmealDishes(setmealDishes);
        }
    }

    /**
     * 分页查询套餐信息
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 使用PageHelper分页插件进行分页查询
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> result = setmealMapper.pageQuerySetmeal(setmealPageQueryDTO);

        return new PageResult(result.getTotal(), result.getResult());
    }
}