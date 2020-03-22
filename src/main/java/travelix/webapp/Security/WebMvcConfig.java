package travelix.webapp.Security;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/fonts/**", "/images/**", "/js/**", "/plugins/**", "/styles/**", "/travelix.ng/images/**", "/travelix.ng/static/js/common/**",
                "/travelix.ng/hoteldetail/styles/**", "/travelix.ng/hoteldetail/plugins/**", "/travelix.ng/hoteldetail/images/**", "/travelix.ng/hoteldetail/js/**",
                "/travelix.ng/tourdestination/plugins/**", "/travelix.ng/tourdestination/styles/**", "/travelix.ng/tourdestination/images/**", "/travelix.ng/tourdestination/js/**", "/travelix.ng/tourdestination/static/**",
                "/travelix.ng/styles/**", "/travelix.ng/js/**", "/travelix.ng/plugins/**", "travelix.ng/fonts/**", "/static/**", "/favico.ico/**")
                .addResourceLocations("classpath:/static/fonts/", "classpath:/static/images/", "classpath:/static/js/", "classpath:/static/plugins/",
                        "classpath:/static/styles/", "classpath:/static/");
    }
}
