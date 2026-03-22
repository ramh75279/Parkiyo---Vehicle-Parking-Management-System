package com.parkiyo.parkiyo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/")
    public String root() {
        return "common/home";
    }

    @GetMapping("/common/home")
    public String home() {
        return "common/home";
    }

    @GetMapping("/{folder}/{page}")
    public String loadPage(@PathVariable String folder,
                           @PathVariable String page) {
        return folder + "/" + page;
    }


}