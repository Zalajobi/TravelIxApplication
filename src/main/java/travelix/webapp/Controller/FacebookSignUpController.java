package travelix.webapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import travelix.webapp.Service.FacebookService;

@Controller
@RequestMapping("/travelix.ng")
public class FacebookSignUpController {

    @Autowired
    private FacebookService facebookService;

    @GetMapping(path = "facebooksignup")
    public RedirectView facebookSignup()
    {
        return new RedirectView(facebookService.createFacebookAuthorizationURL());
    }

    @GetMapping("facebook")
    public void createAccessToken(@RequestParam("code") String code) {
        facebookService.createFacebookAccessToken(code);
        System.out.println(code);
//        return new RedirectView("http://localhost:8080/facebookDetail");
    }

    @RequestMapping(path = "/facebookDetail")
    public String faceBookDetails(Model model) {
        ModelAndView modelAndView = facebookService.getUserDetails();
        return "facebooksignup";
    }
}