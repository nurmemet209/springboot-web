package com.cn.rk.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 12/18/2016.
 * 返回json数据
 */
@Controller
public class MyController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/test")
    @ResponseBody
    public Test test(){
        logger.info("/test");
        return new Test();
    }


    private static class Test {
        String userName="nurmemet";

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        int id=23;
    }
}
