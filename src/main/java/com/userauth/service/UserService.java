package com.userauth.service;

import com.userauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      Optional<UserDetails> userDetails= userRepository.findByEmail(username);
        UserDetails user;

      if (userDetails.isEmpty()) {
          return null;
      }

      user =  userDetails.get();

    String name =  user.getUsername();
    String password = user.getPassword();


     return new User(name, password, user.getAuthorities());

    }
}
