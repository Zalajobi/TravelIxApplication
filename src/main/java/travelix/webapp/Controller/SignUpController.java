package travelix.webapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import travelix.webapp.Model.User;
import travelix.webapp.Service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/travelix.ng")
public class SignUpController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/signup")
    public String signup(Model model) {
        model.addAttribute("userInfo", new User());
        return "signup";
    }


    //Save User
    @PostMapping(path = "/signup")
    public String userSignUp(@Valid User user, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:signup";
        } if (user.getRegion().isBlank()) {
            model.addAttribute("emptyRegion", true);
            return "redirect:signup";
        }
        if (userService.userWithEmailExist(user.getEmail())) {
            model.addAttribute("userWithEmailExist", true);
            return "signup";
        }
        if (userService.userWithUserNameExist(user.getUsername())) {
            model.addAttribute("userWithUsernameExist", true);
            return "signup";
        } else {
            userService.createUser(user);
        }

        return "redirect:login";
    }
}