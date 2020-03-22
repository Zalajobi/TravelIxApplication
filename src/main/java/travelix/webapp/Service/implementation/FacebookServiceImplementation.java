package travelix.webapp.Service.implementation;

import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import travelix.webapp.Service.FacebookService;

@Service
public class FacebookServiceImplementation implements FacebookService {

    final String FACEBOOK_KEY = "2684004441685230";
    final String FACEBOOK_CODE = "b3e9e2b1a2b115ff5332a2824aefec79";
    FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(FACEBOOK_KEY, FACEBOOK_CODE);
    AccessGrant accessGrant;

    String accessToken;

    @Override
    public String createFacebookAuthorizationURL() {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(FACEBOOK_KEY, FACEBOOK_CODE);
        OAuth2Operations operations = connectionFactory.getOAuthOperations();
        OAuth2Parameters parameters = new OAuth2Parameters();
        parameters.setRedirectUri("http://localhost:8080/facebook");
        return operations.buildAuthenticateUrl(parameters);
    }

    @Override
    public void createFacebookAccessToken(String code) {
        FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(FACEBOOK_KEY, FACEBOOK_CODE);
        accessGrant = facebookConnectionFactory.getOAuthOperations().exchangeForAccess(code, "http://localhost:8080/facebook", null);
        accessToken = accessGrant.getAccessToken();
    }

    @Override
    public ModelAndView getUserDetails() {
        Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
        Facebook facebook = connection.getApi();
        String[] fields = { "id, public_profile, email, user_birthday, first_name, last_name"};
        User userProfile = facebook.fetchObject("me", User.class, fields);
        ModelAndView modelAndView = new ModelAndView("details");
//        System.out.println(userProfile.getName());
        modelAndView.addObject("user", userProfile);
        return modelAndView;
    }

    //Connect to Facebook and create an Authentication URI

}
