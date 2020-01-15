package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CenterInfoController {

    @RequestMapping(value = "/center-info", method = RequestMethod.GET)
    public ModelAndView getCenterInfo() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("center_info");
        return modelAndView;
    }
}