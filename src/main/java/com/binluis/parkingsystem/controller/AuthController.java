package com.binluis.parkingsystem.controller;

import com.binluis.parkingsystem.domain.ParkingBoy;
import com.binluis.parkingsystem.domain.ParkingBoyRepository;
import com.binluis.parkingsystem.exception.AppException;
import com.binluis.parkingsystem.models.Role;
import com.binluis.parkingsystem.models.RoleName;
import com.binluis.parkingsystem.models.User;
import com.binluis.parkingsystem.payload.ApiResponse;
import com.binluis.parkingsystem.payload.JwtAuthenticationResponse;
import com.binluis.parkingsystem.payload.LoginRequest;
import com.binluis.parkingsystem.payload.SignUpRequest;
import com.binluis.parkingsystem.domain.RoleRepository;
import com.binluis.parkingsystem.domain.UserRepository;
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
        System.out.println("in!!!!!!!");
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            System.out.println("!!!!Username is already taken!");
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            System.out.println("Email Address already in use!");
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        System.out.println("!!!!beforeUser");
        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

//        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
//                .orElseThrow(() -> new AppException("User Role not set."));

        RoleName roleName=null;
        if(signUpRequest.getRole().equals("PARKINGCLERK")){
            roleName=RoleName.PARKINGCLERK;
        }

        Role userRole=roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException("User Role not set."));
        System.out.println("!!!!role"+userRole);

        user.setRoles(Collections.singleton(userRole));

        User result = null;

        if(roleName.equals(RoleName.PARKINGCLERK)){
            System.out.println("roleName!!!!!"+roleName);
            ParkingBoy parkingBoy=new ParkingBoy(signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(), "available");
            parkingBoyRepository.saveAndFlush(parkingBoy);
            user.setIdInRole(parkingBoy.getId());
            result = userRepository.saveAndFlush(user);
        }
        System.out.println("result!!!!"+result.getName());

        if(result==null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}