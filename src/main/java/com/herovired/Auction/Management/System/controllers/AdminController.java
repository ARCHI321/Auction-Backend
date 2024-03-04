package com.herovired.Auction.Management.System.controllers;

import com.herovired.Auction.Management.System.models.User;
import com.herovired.Auction.Management.System.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @DeleteMapping("/delete-user-by-id/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable long id) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return new ResponseEntity<>("User deleted", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("User not found", HttpStatus.ACCEPTED);
    }

    @PostMapping("/block-user/{username}")
    public ResponseEntity<?> blockUser(@PathVariable String username) {
        var userDataObject = userRepository.findByUserId(username);
        if (userDataObject.isPresent()) {
            userDataObject.get().setBlocked(true);
            return new ResponseEntity<>(userDataObject.get(), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("User not found", HttpStatus.ACCEPTED);

    }

    @PostMapping("/unblock-user/{username}")
    public ResponseEntity<?> unblockUser(@PathVariable String username) {
        var userDataObject = userRepository.findByUserId(username);
        if (userDataObject.isPresent()) {
            userDataObject.get().setBlocked(false);
            return new ResponseEntity<>(userDataObject.get(), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("User not found", HttpStatus.ACCEPTED);
    }

}
