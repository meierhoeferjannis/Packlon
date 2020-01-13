package de.oth.packlon.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {


    @RequestMapping("/")
    public String starten() {

        return "index";
    }
    @RequestMapping("/index")
    public String index(){
        return "index";
    }


}
