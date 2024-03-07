package org.epm.bs4.controller;

import lombok.AllArgsConstructor;
import org.epm.bs4.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class WebController {
    private final UserRepository userRepository;

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("status", "Alive!");
        return "info";
    }

    @GetMapping(path = "/about")
    public String about(Model model) {
        return "about";
    }

    @GetMapping(path = "/admin")
    public String admin(Model model) {
        model.addAttribute("users", userRepository.findUserByEnabledIsFalse());
        return "admin";
    }

    @GetMapping(path = "/login")
    public String login() {
        return "login";
    }
}