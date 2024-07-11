
package com.paynet.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.net.URI;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
/*
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

 */
/*
private LogoutSuccessHandler oidcLogoutSuccessHandler() {
    OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
            new OidcClientInitiatedLogoutSuccessHandler(
                    this.clientRegistrationRepository);

    oidcLogoutSuccessHandler.setPostLogoutRedirectUri(
            String.valueOf(URI.create("http://localhost:8080/login")));

    return oidcLogoutSuccessHandler;
}

 */

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((requests) -> requests.requestMatchers
                (PathRequest.toStaticResources().atCommonLocations()).permitAll())
                .authorizeHttpRequests(auth -> {
                    //auth.requestMatchers("/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/inscription").permitAll();
                    auth.requestMatchers("/addUserPayApp").permitAll();
                    auth.requestMatchers("/user").hasRole("USER");
                    auth.anyRequest().authenticated();
                    //}).formLogin(Customizer.withDefaults()).build();
                }).formLogin(form -> form
                        .loginPage("/showLoginPage")
                        .loginProcessingUrl("/authenticateTheUser")
                        .defaultSuccessUrl("/", true)
                        .permitAll()).oauth2Login(form -> form
                        .loginPage("/showLoginPage")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(LogoutConfigurer::permitAll)
                .build();
    }


 /*
 @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> {
                    //auth.requestMatchers("/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/user").hasRole("USER");
                    auth.anyRequest().authenticated();
                    //}).formLogin(Customizer.withDefaults()).build();
                }).formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/")
                        .defaultSuccessUrl("/add-transaction.html", true)
                        .failureUrl("/login.html?error=true")
                        .failureHandler(authenticationFailureHandler())).oauth2Login(Customizer.withDefaults())
                .logout((logout) -> logout
                        .logoutSuccessHandler(oidcLogoutSuccessHandler())).build();
    }


 @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
            auth.requestMatchers("/user").hasRole("USER");
            //auth.requestMatchers("/login*").permitAll();
            auth.anyRequest().authenticated();
        //}).formLogin(Customizer.withDefaults()).build();
        }).formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/")
                        .defaultSuccessUrl("/add-transaction.html", true)
                        .failureUrl("/login.html?error=true")
                        .failureHandler(authenticationFailureHandler()))
                //.oauth2Login(Customizer.withDefaults())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(oidcLogoutSuccessHandler())
                )
                .build();
    }

  @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> {
            //auth.requestMatchers("/admin/**").hasRole("ADMIN");
            auth.requestMatchers("/user").hasRole("USER");
            auth.anyRequest().authenticated();
        //}).formLogin(Customizer.withDefaults()).build();
        }).formLogin(Customizer.withDefaults()).oauth2Login(Customizer.withDefaults())
                .logout((logout) -> logout
                .logoutSuccessHandler(oidcLogoutSuccessHandler())).build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole("ADMIN")
                .antMatchers("/anonymous*")
                .anonymous()
                .antMatchers("/login*")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/homepage.html", true)
                .failureUrl("/login.html?error=true")
                .failureHandler(authenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(oidcLogoutSuccessHandler());
    }


*/



}
