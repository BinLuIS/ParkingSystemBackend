package com.binluis.parkingsystem.controller;

import com.binluis.parkingsystem.domain.*;
import com.binluis.parkingsystem.exception.ResourceNotFoundException;
import com.binluis.parkingsystem.models.ModifyUserRequest;
import com.binluis.parkingsystem.models.ParkingBoyResponse;
import com.binluis.parkingsystem.models.RoleName;
import com.binluis.parkingsystem.models.User;
import com.binluis.parkingsystem.payload.*;
import com.binluis.parkingsystem.security.UserPrincipal;
import com.binluis.parkingsystem.security.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParkingBoyRepository parkingBoyRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('PARKINGCLERK')")
    public User getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        System.out.println("?????"+currentUser.getId());
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        Optional<User> user=userRepository.findById(currentUser.getId());
        if(user.get().getStatus().equals("freeze")){
            return null;
        }
        return user.get();
    }

    @GetMapping("/user/manager")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public User getCurrentUser_manager(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        Optional<User> user=userRepository.findById(currentUser.getId());
        if(user.get().getStatus().equals("freeze")){
            return null;
        }
        return user.get();
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

//    @GetMapping(path = "/user/{id}/parkingclerks")
//    public ResponseEntity<ParkingBoy> getAllParkingBoys(@PathVariable Long id) {
//        Optional<User> user = userRepository.findById(id);
//        if(!user.isPresent()){
//            return ResponseEntity.badRequest().build();
//        }
//        ParkingBoy parkingBoy=parkingBoyRepository.findOneByName(user.get().getName());
//        return ResponseEntity.ok(parkingBoy);
//    }
//    @GetMapping("/users/{username}")
//    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
//
//        long pollCount = pollRepository.countByCreatedBy(user.getId());
//        long voteCount = voteRepository.countByUserId(user.getId());
//
//        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), pollCount, voteCount);
//
//        return userProfile;
//    }

    @GetMapping(path = "/users")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping(path = "/users/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> users = userRepository.findById(id);
        if(!users.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users.get());
    }

    @PatchMapping(path = "/users/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity modifyUser(@PathVariable Long id, @RequestBody ModifyUserRequest request){
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()){
            return ResponseEntity.notFound().build();
        }

        user.get().getRoles().forEach(role->{
                if(role.getName().equals(RoleName.ROLE_MANAGER)){
                    Manager manager=managerRepository.findOneById(user.get().getIdInRole());
                    if(manager!=null) {
                        if (request.getName() != null) {
                            manager.setName(request.getName());
                        }
                        if (request.getPhoneNumber() != null) {
                            manager.setPhoneNumber(request.getPhoneNumber());
                        }
                        if (request.getEmail() != null) {
                            manager.setEmail(request.getEmail());
                        }
                        managerRepository.saveAndFlush(manager);
                    }
                }else if(role.getName().equals(RoleName.ROLE_PARKINGCLERK)){
                    Optional<ParkingBoy> parkingBoy=parkingBoyRepository.findById(user.get().getIdInRole());
                    if(parkingBoy.isPresent()) {
                        if (request.getName() != null) {
                            parkingBoy.get().setName(request.getName());
                        }
                        if (request.getPhoneNumber() != null) {
                            parkingBoy.get().setPhoneNumber(request.getPhoneNumber());
                        }
                        if (request.getEmail() != null) {
                            parkingBoy.get().setEmail(request.getEmail());
                        }
                        if (request.getStatus() != null) {
                            parkingBoy.get().setStatus(request.getStatus());
                        }
                        parkingBoyRepository.saveAndFlush(parkingBoy.get());
                    }
                }


        });

        if(request.getUsername()!=null) {
            user.get().setUsername(request.getUsername());
        }
        if(request.getName()!=null) {
            user.get().setName(request.getName());
        }
        if(request.getPhoneNumber()!=null) {
            user.get().setPhoneNumber(request.getPhoneNumber());
        }
        if(request.getEmail()!=null) {
            user.get().setEmail(request.getEmail());
        }
        if(request.getStatus()!=null) {
            user.get().setStatus(request.getStatus());
        }
        if(request.getPassword()!=null) {
            user.get().setPassword(passwordEncoder.encode(request.getPassword()));

        }

        userRepository.saveAndFlush(user.get());
        return ResponseEntity.created(URI.create("/api/users/"+id)).body(user);

    }
}