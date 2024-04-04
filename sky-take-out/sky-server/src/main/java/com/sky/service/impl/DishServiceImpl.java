package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName: DishServiceImpl
 * Package: com.sky.service.impl
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/4 10:02
 * @Version 1.0
 */
/*为什么在实现类上使用@Service？
        使用@Service注解的目的是将Spring管理的bean的角色或意图表达得更明确。
        尽管理论上可以将@Service注解放在接口上，但这样做并不符合Spring的推荐做法，也不符合依赖注入（DI）的一般原则。
        Spring容器在运行时需要创建bean的实例，而接口本身不能被实例化，只有实现了接口的具体类才能被实例化。
        因此，将@Service放在具体的实现类上，可以让Spring准确地知道应该实例化哪个类。*/
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired // 服务层的实现类中使用@Autowired来自动注入这个mapper的实现
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品及其口味
     *
     * @param dishDTO
     */
    /*这里可能会涉及到两张表的操作（菜品表和菜品口味表），因此需要使用@Transactional注解来保证事务的一致性。*/
    @Transactional // 开启事务（注意：需要在SkyApplication类上添加@EnableTransactionManagement注解）
    @Override
    public void addDishWithFlavors(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish); // 将dishDTO对象属性复制到dish对象中

        // 向菜品表插入1条数据
        dishMapper.insertDish(dish);
        // 获取刚插入的菜品的主键id
        Long dishId = dish.getId();

        // 向菜品口味表插入多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        /*  flavors != null && !flavors.isEmpty() 是Java中检查一个集合是否不为空 且 至少包含一个元素的推荐方式。
        这种检查方式简短且能清楚地表达意图，避免了NullPointerException的风险，并确保了集合至少有一个元素可以操作。
            这种方式分为两部分：
                flavors != null：首先检查flavors引用是否不为null，确保后续的操作不会因尝试访问null引用而抛出NullPointerException。
                                 一个非null的集合可能是空的，这意味着它没有包含任何元素。
                !flavors.isEmpty()：接着检查flavors集合是否不为空，即确保集合至少包含一个元素。isEmpty()方法返回true表示集合为空，因此前面加上!操作符表示集合不为空。*/
        if (flavors != null && !flavors.isEmpty()) {
            // 遍历flavors集合，为每一个口味对象设置dishId属性
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));

            // 向菜品口味表插入多条数据
            dishFlavorMapper.insertBatchDishFlavor(flavors);
        }


    }
}
