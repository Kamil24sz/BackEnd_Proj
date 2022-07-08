package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    @Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .authorizeRequests()
                    .mvcMatchers(HttpMethod.GET, "/api/**").permitAll()
                    .mvcMatchers(HttpMethod.POST, "/api/**").permitAll()
                    .mvcMatchers(HttpMethod.PUT, "/api/**").permitAll()

                    //.mvcMatchers(HttpMethod.GET, "/api/ticket/**").hasAnyAuthority("ROLE_USER","USER")
                    //.mvcMatchers(HttpMethod.POST, "/api/ticket/**").hasAnyAuthority("ROLE_USER","USER")
                    //.mvcMatchers(HttpMethod.PUT, "/api/ticket/**").hasAnyAuthority("ROLE_USER","USER")
//
                    //.mvcMatchers(HttpMethod.GET, "/api/admin/**").hasRole("ADMIN")
                    //.mvcMatchers(HttpMethod.POST, "/api/admin/**").hasRole("ADMIN")
                    //.mvcMatchers(HttpMethod.PUT, "/api/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic();
        }
    }
