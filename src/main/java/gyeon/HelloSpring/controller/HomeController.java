package gyeon.HelloSpring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // / : 홈페이지로 바로 들어오면 home.html을 찾아서 호출.
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
