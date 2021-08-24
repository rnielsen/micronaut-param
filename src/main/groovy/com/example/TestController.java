package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import java.util.Map;

@Controller
public class TestController {
    @Post("/stringParams")
    public Map stringParams(String a, String b) {
        return Map.of("a", a, "b", b);
    }
}
