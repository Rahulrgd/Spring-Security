package com.in28minutes.learningspringsecurity.basic;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class BasicAuthSecurityConfiguration {

  @Bean
  @Order(SecurityProperties.BASIC_AUTH_ORDER)
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests(auth -> {
      auth
        .requestMatchers("/users")
        .hasRole("USER")
        .requestMatchers("/admin")
        .hasRole("ADMIN")
        .anyRequest()
        .authenticated();
    });
    http.sessionManagement(session ->
      session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );
    // http.formLogin();
    http.httpBasic();
    http.csrf().disable();
    http.headers().frameOptions().sameOrigin();
    return http.build();
  }

  //   Creating new user in inmemory
  //   @Bean
  //   public UserDetailsService userDetailsService() {
  //     var user = User
  //       .withUsername("in28minutes")
  //       .password("{noop}dummy")
  //       .roles("USER")
  //       .build();
  //     var admin = User
  //       .withUsername("admin")
  //       .password("{noop}dummy")
  //       .roles("ADMIN")
  //       .build();
  //     return new InMemoryUserDetailsManager(user, admin);
  //   }

  //   Executing Default User Schema DDL to create User table in H2 database during thing startup of the application
  @Bean
  public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
      .setType(EmbeddedDatabaseType.H2)
      .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
      .build();
  }

  @Bean
  public UserDetailsService userDetailsService(DataSource dataSource) {
    var user = User
      .withUsername("in28minutes")
      //   .password("{noop}dummy")
      .password("dummy")
      .passwordEncoder(str -> passwordEncoder().encode(str))
      .roles("USER", "ADMIN")
      .build();
    var admin = User
      .withUsername("admin")
      //   .password("{noop}dummy")
      .password("dummy")
      .passwordEncoder(str -> passwordEncoder().encode(str))
      .roles("ADMIN")
      .build();

    var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
    jdbcUserDetailsManager.createUser(user);
    jdbcUserDetailsManager.createUser(admin);
    return jdbcUserDetailsManager;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
