package ua.com.foxminded.task.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    private Logger logger;

    @Autowired
    public MainController(Logger logger) {
        this.logger = logger;
    }

    @GetMapping
    public String main(Model model) {
        logger.debug("main()");
        model.addAttribute("title", "University timetable project");
        return "main";
    }
}
