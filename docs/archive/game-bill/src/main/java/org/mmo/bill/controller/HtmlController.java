package org.mmo.bill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {

    @GetMapping("/")
    public String blog(Model model) {
        model.addAttribute("title", "Blog");
        return "blog";
    }
}
