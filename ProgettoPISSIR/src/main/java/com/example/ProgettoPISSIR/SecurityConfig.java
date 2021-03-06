package com.example.ProgettoPISSIR;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .antMatcher("/**").authorizeRequests()
                .antMatchers(new String[]{"/", "/not-restricted", "/index.html" ,"/SmartLocker1Sblocco.html", "/sbloccoSmartLocker1", "/SmartLocker3Sblocco.html","/sbloccoSmartLocker3"}).permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login();
    }
}
