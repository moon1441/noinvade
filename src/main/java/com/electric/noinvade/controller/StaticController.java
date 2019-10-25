package com.electric.noinvade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {
    @GetMapping({"/", "/family-view"})
    public String index() {
        return "index";
    }
}
