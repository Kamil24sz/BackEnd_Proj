package com.example.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class HomeController {

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "Hello World!";
    }

    @RequestMapping("/example")
    public String plik(){
        return "index";
    }

    @RequestMapping("/test/{name}")
    @ResponseBody
    public String test(@PathVariable String name, @RequestParam String query){
        return "Hi " + name + "! "+query;
    }

    @RequestMapping("/animals/cat")
    public String cat(){
        return "cat";
    }

}
