package com.tpe.cookerytech.security;

import com.tpe.cookerytech.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfing {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //filtreleme islemlerine ekleme yapmak icin standartta gelenlerin disinda
        http.csrf().disable(). //restfull api oldugu icin csrf i disable ediyoruz
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and(). //cors islemlerinde delete vb. islemlerde meydana gelen sorunu ortadan kaldirmak icin alttaki satir eklendi
                authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/**").permitAll().and().
                authorizeRequests().
                antMatchers("/login",
                        "/register",
                        "/actuator/info",
                        "/actuator/health",
                        "/forgot-password",
                        "/reset-password",
                        "/contact-messages")
                .permitAll().anyRequest().authenticated(); // bunlar disinda gelenleri authenticated yap
        //!!!AuthTokenFilter yazdiktan sonra addFilter yazilacak
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class); //15 standart filtreye 16. yi burda ekledik

        return http.build();
    }

    //!!!Encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    //!!!Provider
    @Bean
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return  authenticationProvider;
    }

    //!!! AuthenticationManager
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception{
        return http.getSharedObject(AuthenticationManagerBuilder.class).
                authenticationProvider(authProvider()).
                build();

    }

    //!!!AuthTokenFilter(JWT token ureten ve valide eden class
    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

}
