package com.herovired.Auction.Management.System.SecurityConfigs;

import com.herovired.Auction.Management.System.SecurityConfigs.JwtAuthFilter;
import com.herovired.Auction.Management.System.services.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    // User Creation
    @Bean
    public UserDetailsService userDetailsService() {
        return new JwtUserDetailsService();
    }

    // Configuring HttpSecurity
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Inside authorization");
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers( HttpMethod.GET,"/ws/**").permitAll()
                .requestMatchers( HttpMethod.POST,"/ws/**").permitAll()
                .requestMatchers( HttpMethod.GET,"/notification/**").permitAll()
                .requestMatchers( HttpMethod.POST,"/notification/**").permitAll()
                .requestMatchers( HttpMethod.GET,"/api/**").permitAll()
                .requestMatchers( HttpMethod.POST,"/api/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                .requestMatchers(HttpMethod.PUT, "/user/complete-details").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/forgot-password/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/forgot-password/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/anonymous/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/user/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/otp/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/otp/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/auction/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/auction/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/auction/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/auction/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/categories/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/auction-registrations/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/auction-registrations/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/auction-registrations/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/bid/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/bid/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/bid/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/payment/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/payment/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/payment/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/transactions/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/transactions/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/transactions/**").hasAnyRole("USER", "ADMIN")

                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Password Encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}

