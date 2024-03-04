package com.herovired.Auction.Management.System.services;

import java.util.ArrayList;
import java.util.Optional;

import com.herovired.Auction.Management.System.repositories.AuthorityRepository;
import com.herovired.Auction.Management.System.repositories.UserDataRepository;
import com.herovired.Auction.Management.System.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //System.out.println("username" + username);
        var userDetail = userRepository.findByUserName(username);
        //System.out.println("userDetail" + userDetail);
        var authority = authorityRepository.findByUserName(username);
        //System.out.println("authorityUser" + authority);
        var authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(authority.get().getAuthority()));
        //System.out.println("authorities" + authorities);
        if (userDetail.isPresent()) {
            return new User(userDetail.get().getUserName(), userDetail.get().getUserPassword(),
                    authorities);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}