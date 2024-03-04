//package com.herovired.Auction.Management.System.SecurityConfigs;
//
////import com.herovired.Library.Management.System.models.User;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.provisioning.UserDetailsManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class InMemoryConfiguration {
//
//    String queryToFetchUsers = "select user_name, user_password, true from user where user_name = ?";
//    String queryToFetchAuthorities = "select username, authority from authorities where username = ?";
//
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
//
//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        System.out.println("Inside authentication");
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
//        jdbcUserDetailsManager.setDataSource(dataSource);
//        jdbcUserDetailsManager.setUsersByUsernameQuery(queryToFetchUsers);
//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(queryToFetchAuthorities);
//        return jdbcUserDetailsManager;
//    }
//}
////
////    // @Bean
////    // public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
////    // System.out.println("Inside authentication");
////    // UserDetails user = User.builder()
////    // .username("user")
////    // .password("{noop}abc@123")
////    // .roles("USER")
////    // .build();
////    // UserDetails manager = User.builder()
////    // .username("manager")
////    // .password("{noop}abc@1234")
////    // .roles("MANAGER")
////    // .build();
////    //
////    // UserDetails admin = User.builder()
////    // .username("admin")
////    // .password("{noop}abc@12345")
////    // .roles("ADMIN")
////    // .build();
////    // return new InMemoryUserDetailsManager(user,manager,admin);
////    //
////    // }
////}
