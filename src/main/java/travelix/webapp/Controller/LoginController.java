package travelix.webapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "travelix.ng")
public class LoginController {

    @Autowired
    private HttpSession session;

    @GetMapping(path = "login")
    public String login()
    {
        return "login";
    }
}