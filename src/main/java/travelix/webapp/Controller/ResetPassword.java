package travelix.webapp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "travelix.ng")
public class ResetPassword {

    @RequestMapping(path = "/createToken", method = RequestMethod.GET)
    public String resetPassword() {
//        Twilio
        return "createToken";
    }
}