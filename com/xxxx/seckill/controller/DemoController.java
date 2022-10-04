package com.xxxx.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shkstart
 * @create 2022-09-08 17:56
 */

@Controller
@RequestMapping("/demo")
public class DemoController {
    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("name","xxxx");
        return "hello";
    }
}
