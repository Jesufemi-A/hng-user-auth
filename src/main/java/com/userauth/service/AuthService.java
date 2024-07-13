package com.userauth.service;

import com.userauth.dto.RegisterDto;
import com.userauth.model.Organisation;
import com.userauth.model.User;
import com.userauth.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.logging.Logger;


@Service
public class UserService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    Logger logger = Logger.getLogger("RegisterController.class");


    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto register) {

        try {
            User user = mapUser(register);
            String hash = passwordEncoder.encode(user.getPassword());
            user.setPassword(hash);


            userRepository.save(user);

            return ResponseEntity.status(201).body("ok");
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }






    }


    public Organisation createDefaultOrganisation(){

    }





    public User mapUser(RegisterDto reg) {
        User user = null;

        user.setFirstName(reg.getFirstName());
        user.setLastName(reg.getLastName());
        user.setEmail(reg.getEmail());
        user.setPassword(reg.getPassword());
        user.setPhone(reg.getPhone());

        return user;
    }
}

