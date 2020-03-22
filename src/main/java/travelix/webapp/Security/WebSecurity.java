package travelix.webapp.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    private String[] PUBLIC_ADDRESS = {
            "/travelix.ng",
            "/travelix.ng/signup",
            "/travelix.ng/login",
            "/about",
            "signup",
            "/static/fonts/**",
            "/resources/static/js/**",
            "/plugins/**",
            "/static/styles/**",
            "/styles/bootstrap4/**",
            "/styles/Login/**",
            "/styles/signup/**",
            "/static/images/signuplogin/**",
            "/resources/**",
            "/images/**"};

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests().antMatchers(PUBLIC_ADDRESS).permitAll()
                .and().formLogin().loginPage("/travelix.ng/login").permitAll().defaultSuccessUrl("/travelix.ng/").and().logout().logoutSuccessUrl("/travelix.ng/login")
                .and().csrf().disable().cors().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery("select user_name as principal, password as credentials, true from user where user_name=?")
                .authoritiesByUsernameQuery("select user_name as principal, role_name as role from user_role where user_name=?").passwordEncoder(passwordEncoder()).rolePrefix("ROLE_");
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
