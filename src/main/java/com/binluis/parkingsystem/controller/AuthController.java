package com.binluis.parkingsystem.controller;

import com.binluis.parkingsystem.domain.*;
import com.binluis.parkingsystem.exception.AppException;
import com.binluis.parkingsystem.models.Role;
import com.binluis.parkingsystem.models.RoleName;
import com.binluis.parkingsystem.models.User;
import com.binluis.parkingsystem.payload.ApiResponse;
import com.binluis.parkingsystem.payload.JwtAuthenticationResponse;
import com.binluis.parkingsystem.payload.LoginRequest;
import com.binluis.parkingsystem.payload.SignUpRequest;
import com.binluis.parkingsystem.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    ParkingBoyRepository parkingBoyRepository;

    @Autowired
    ManagerRepository managerRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        RoleName roleName=null;
        if(signUpRequest.getRole().equals("PARKINGCLERK")){
            roleName=RoleName.ROLE_PARKINGCLERK;
        }
        if(signUpRequest.getRole().equals("MANAGER")){
            roleName=RoleName.ROLE_MANAGER;
        }

        Role userRole=roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));
        user.setPhoneNumber(signUpRequest.getPhoneNumber());

        User result = null;

        if(roleName.equals(RoleName.ROLE_PARKINGCLERK)){
            ParkingBoy parkingBoy=new ParkingBoy(signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(), "available");
            user.setIdInRole(parkingBoy.getId());
            result = userRepository.saveAndFlush(user);
            if(result==null){
                parkingBoyRepository.saveAndFlush(parkingBoy);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        if(roleName.equals(RoleName.ROLE_MANAGER)){
            Manager manager=new Manager(signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber());
            user.setIdInRole(manager.getId());
            result = userRepository.saveAndFlush(user);
            if(result==null){
                managerRepository.saveAndFlush(manager);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        


        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}