package travelix.webapp.Service;

import org.springframework.web.servlet.ModelAndView;

public interface FacebookService
{


    public String createFacebookAuthorizationURL();

    //Generate Token
    public void createFacebookAccessToken(String code);

    //Get user profile
    public ModelAndView getUserDetails();
}
