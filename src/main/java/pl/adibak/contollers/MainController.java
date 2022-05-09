package pl.adibak.contollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class MainController {

    @GetMapping("/")
    String index() {
        return "index";

    }
}
