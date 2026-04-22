package com.parkiyo.parkiyo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicPageController {

    @GetMapping({"/", "/home"})
    public String home() {
        return "public/home";
    }

    @GetMapping("/features")
    public String features() {
        return "public/features";
    }

    @GetMapping("/solutions")
    public String solutions() {
        return "public/solutions";
    }

    @GetMapping("/analytics")
    public String analytics() {
        return "public/analytics";
    }

    @GetMapping("/faq")
    public String faq() {
        return "public/faq";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "public/privacy";
    }
}
