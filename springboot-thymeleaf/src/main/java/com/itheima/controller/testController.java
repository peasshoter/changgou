package com.itheima.controller;

import com.itheima.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/tempates")
public class testController {
    @GetMapping("/demo")
    public String demo1(Model model) {
        model.addAttribute("message", "你好");
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "zs", "sz"));
        users.add(new User(2, "zs", "sz"));
        users.add(new User(3, "zs", "sz"));
        users.add(new User(4, "zs", "sz"));
        model.addAttribute("user", users);
        return "demo1";
    }

    @GetMapping("/demo2")
    public String demo2(Model model) {
        model.addAttribute("message", "你好啊");
        return "demo1";
    }
}
