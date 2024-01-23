package com.in28minutes.learningspringsecurity.basic;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class BasicAuthSecurityConfiguration {

  @Bean
  @Order(SecurityProperties.BASIC_AUTH_ORDER)
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests(auth -> {
      auth.anyRequest().authenticated();
    });
    http.sessionManagement(session ->
      session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );
    // http.formLogin();
    http.httpBasic();
    http.csrf().disable();
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    var user = User.withUsername("in28minutes").password("{noop}dummy").roles("USER").build();
    var admin = User.withUsername("admin").password("{noop}dummy").roles("ADMIN").build();
    return new InMemoryUserDetailsManager(user, admin);
  }
}
