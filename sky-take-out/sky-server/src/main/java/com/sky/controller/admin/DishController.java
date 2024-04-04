package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: DishController
 * Package: com.sky.controller.admin
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/4 9:02
 * @Version 1.0
 */
/*@RestController是Spring框架中一个用于构建RESTful Web服务的注解。它是@Controller和@ResponseBody注解的结合体，意味着它不仅接收HTTP请求，
同时方法的响应会自动转换为JSON或XML格式返回给客户端。这样的设计简化了开发者在创建RESTful服务时的工作量，无需在每个方法上分别使用@ResponseBody注解。*/
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "后台菜品管理接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增菜品", notes = "新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO
    /*@RequestBody DishDTO dishDTO参数表示这个方法期望从请求的body中获取菜品数据，并且Spring将自动把JSON数据映射成DishDTO对象
    * 使用@RequestBody注解时，Spring会将HTTP请求的内容区中的JSON或XML数据反序列化为Java对象，因此请求的Content-Type必须为application/json或application/xml等相应的媒体类型*/) {
        log.info("新增菜品：{}", dishDTO);

        // 调用业务逻辑层方法，新增菜品及其口味
        dishService.addDishWithFlavors(dishDTO);

        // 返回成功结果
        return Result.success();
    }
}
