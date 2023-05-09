package com.example.digitalmenuapi.configuration;

import com.example.digitalmenuapi.service.AdminMenuItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * security configuration class for basic auth implementation and setup.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  /**
   * used to inject an instance of the adminservice class.
   */
  @Autowired
  private AdminMenuItemsService adminMenuItemsService;

  /**
   * creates a BCryptPasswordEncoder instance,
   * which is a password encoder used to securely hash and store user passwords.
   */
  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }



  /**
   * created an InMemoryUserDetailsManager bean
   * and I added an admin user with the username "admin" and hashed password "staff123"
   * with the role "ADMIN".
   */
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("staff123"))
            .roles("ADMIN")
            .build();

    return new InMemoryUserDetailsManager(admin);
  }

  /**
   * I created a filter chain method that specifies which requests are allowed or blocked.
   * allowing unauthenticated access to the GET and POST request created earlier,
   * requests to "/admin/menu" and "/admin/**" require basic authentication with the "ADMIN" role.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
              .authorizeRequests()
              .requestMatchers(String.valueOf(RequestMethod.GET), "/user/menuList").permitAll()
              .requestMatchers(String.valueOf(RequestMethod.POST), "/menu/create").permitAll()
              .requestMatchers(String.valueOf(RequestMethod.POST), "/admin/menu").hasRole("ADMIN")
              .requestMatchers(String.valueOf(RequestMethod.DELETE), "/admin/**").hasRole("ADMIN")
              .anyRequest().authenticated()
              .and()
              .httpBasic()
              .and()
              .csrf().disable();
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers("/v3/api-docs",
          "/swagger-ui.html", "/swagger-ui/**");
  }
}


