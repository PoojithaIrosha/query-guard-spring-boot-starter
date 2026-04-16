package io.github.poojithairosha.query_guard_spring_boot_starter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QueryGuardUiController {
    @GetMapping({
            "/queryguard-ui",
            "/queryguard-ui/",
            "/queryguard-ui/dashboard",
            "/queryguard-ui/trace/{traceId}"
    })
    public String forwardQueryGuardUi() {
        return "forward:/queryguard-ui/index.html";
    }
}
