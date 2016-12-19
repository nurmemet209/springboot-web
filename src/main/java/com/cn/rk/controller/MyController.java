package com.cn.rk.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 12/18/2016.
 */
@RestController
public class MyController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/test")
    public String test(){
        logger.info("/test");
        return "testdfdfdsfse";
    }
}
