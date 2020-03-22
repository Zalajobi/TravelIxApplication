package travelix.webapp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/travelix.ng")
public class BlogController {

    @GetMapping( path = "blog")
    public String blog() {
        return "blog";
    }
}
