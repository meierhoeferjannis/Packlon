package de.oth.packlon.controller;

import de.oth.packlon.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class LoginController {
@Autowired
private DeliveryService deliveryService;
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() {

        return "login";
    }


}
