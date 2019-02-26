package com.cetc.web.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestResource {

    @RequestMapping("/test")
    public String test(Principal principal) {
        return "test" + principal;
    }
}
