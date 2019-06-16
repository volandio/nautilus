package com.nautilus.controller;

import com.nautilus.util.InfoOfCurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final InfoOfCurrentUser infoOfCurrentUser;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/")
    public String index() {
        if (infoOfCurrentUser.isAdmin()) {
            return "admin";
        }
        return "index";
    }
}
