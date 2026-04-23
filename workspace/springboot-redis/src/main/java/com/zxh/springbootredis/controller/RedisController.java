package com.zxh.springbootredis.controller;

import com.alibaba.fastjson.JSON;
import com.zxh.springbootredis.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/aaa")
    public String testA(){
        User user = new User();
        user.setName("张三");
        user.setAge(18);
        redisTemplate.opsForValue().set("aaa","bbb");
        redisTemplate.opsForValue().set("user", JSON.toJSONString(user));
        return "success";
    }
}
