package com.app.oauthor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true,securedEnabled=true,jsr250Enabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    //@Autowired
    //private UserRepository userRepository;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager()throws Exception{
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder)throws Exception{
        builder.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());
    }

    //@Bean
    //public UserDetailService userDetailService(){

    //}

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity webSecurity)throws Exception{
        webSecurity.ignoring().antMatchers("/oauth/check_token","/user/login");
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
