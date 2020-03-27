package travelix.webapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import travelix.webapp.Service.ResetPasswordService;
import travelix.webapp.Service.UserService;

@Controller
@RequestMapping(path = "travelix.ng")
public class ResetPassword {

    @Autowired
    private ResetPasswordService resetPasswordService;
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/createToken", method = RequestMethod.GET)
    public String resetPassword() {
//        Twilio
        return "createToken";
    }

    @RequestMapping(path = "/createToken", method = RequestMethod.POST)
    public String resetPassword(Model model, @RequestParam(name = "email") String email) {
        if (!userService.userWithEmailExist(email)) {
            model.addAttribute("invalidEmail", true);
        } else {
            resetPasswordService.resetPassword(email);
        }
        System.out.println(email);
        return "createToken";
    }
}