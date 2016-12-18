package com.cn.rk.com.cn.rk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 12/18/2016.
 */
@RestController
public class MyController {

    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
