package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: shopController
 * Package: com.sky.controller.admin
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/6 下午12:14
 * @Version 1.0
 */
@RestController("userShopController") // 有多个相同类型的控制器需要区分时，给这个控制器指定了一个特定的Bean名称。
@RequestMapping("/user/shop")
@Api(tags = "店铺操作接口")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    public final static String SHOP_STATUS = "SHOP_STATUS";

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation(value = "获取营业状态", notes = "获取店铺营业状态，1-营业，0-打烊")
    public Result<Integer> getStatus() {
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);

        log.info("获取店铺营业状态：{}", shopStatus == 1 ? "营业" : "打烊");

        return Result.success(shopStatus);
    }
}