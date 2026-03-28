package com.parkiyo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicPageController {

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/features")
    public String features() {
        return "features";
    }

    @GetMapping("/solutions")
    public String solutions() {
        return "solutions";
    }

    @GetMapping("/analytics")
    public String analytics() {
        return "analytics";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }
}
