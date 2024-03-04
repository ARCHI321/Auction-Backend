package com.herovired.Auction.Management.System.controllers;

import com.herovired.Auction.Management.System.Authentication.UserAuthenticationObject;
import com.herovired.Auction.Management.System.Authentication.UserLoginRequestObject;
import com.herovired.Auction.Management.System.exception.CustomResponse;
import com.herovired.Auction.Management.System.exception.DuplicateUserException;
import com.herovired.Auction.Management.System.exception.UserNotFoundException;
import com.herovired.Auction.Management.System.models.Admin;
import com.herovired.Auction.Management.System.models.Authority;
import com.herovired.Auction.Management.System.models.User;
import com.herovired.Auction.Management.System.models.UserData;
import com.herovired.Auction.Management.System.repositories.AdminRepository;
import com.herovired.Auction.Management.System.repositories.AuthorityRepository;
import com.herovired.Auction.Management.System.repositories.UserDataRepository;
import com.herovired.Auction.Management.System.repositories.UserRepository;
import com.herovired.Auction.Management.System.services.JwtService;
import com.herovired.Auction.Management.System.services.UserDataServices;
import com.herovired.Auction.Management.System.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServices userServices;

    @Autowired
    private UserDataServices userDataServices;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult resultSet) {
         var errorMap = userServices.validateRequestBody(user, resultSet);
         if (errorMap.size() != 0) {
         return new ResponseEntity(errorMap, HttpStatus.BAD_REQUEST);
         }
        var userId = user.getUserId();
        var username = user.getUserName();

        var existingUserName = userRepository.findByUserId(userId);
        if (existingUserName.isPresent() && Objects.equals(username, existingUserName.get().getUserName())) {
            throw new DuplicateUserException("user " + username + " already exists");
        }
        var password1 = user.getUserPassword();
        user.setUserPassword(passwordEncoder.encode(password1));
        var saveUserCategory = userRepository.save(user);
        var authority = new Authority();
        authority.setAuthority("ROLE_USER");
        authority.setUser(saveUserCategory);
        authorityRepository.save(authority);

        UserData userdata = new UserData();
        userdata.setUserId(user.getUserId());
        userdata.setUserName(user.getUserName());
        userdata.setName(user.getName());
        userdata.setEmail(user.getEmail());
        userdata.setUserPassword(user.getUserPassword());

        userDataRepository.save(userdata);

        return new ResponseEntity(saveUserCategory, HttpStatus.ACCEPTED);
    }

    @PutMapping("/complete-details")
    public ResponseEntity<?> completeDetails(@Valid @RequestBody UserData userData, BindingResult resultSet) {
        var errorMap = userDataServices.validateRequestBody(userData, resultSet);
        if (errorMap.size() != 0) {
            return new ResponseEntity(errorMap, HttpStatus.BAD_REQUEST);
        }
        var userName = userData.getUserName();
        System.out.println(userName);
        System.out.println(userData);
        var existingUserNickName = userDataRepository.findByUserName(userName);
        System.out.println(existingUserNickName);
        if (!existingUserNickName.isPresent()) {
            throw new UserNotFoundException("user " + userName + " not found");
        }
        var getExistingUserData = userDataRepository.findByUserName(userName);
        if (getExistingUserData.isPresent()) {
            getExistingUserData.get().setAddress(userData.getAddress());
            getExistingUserData.get().setPhoneNumber(userData.getPhoneNumber());
            getExistingUserData.get().setUpiId(userData.getUpiId());
            getExistingUserData.get().setAddressPublic(userData.isAddressPublic());
            getExistingUserData.get().setEmailPublic(userData.isEmailPublic());
            getExistingUserData.get().setPhonePublic(userData.isPhonePublic());
            getExistingUserData.get().setUpiPublic(userData.isUpiPublic());
        }

        System.out.println(getExistingUserData);
        UserData updatedUserData = userDataRepository.save(getExistingUserData.get());
        System.out.println(updatedUserData);
        return new ResponseEntity(updatedUserData, HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public UserAuthenticationObject attemptLogin(@RequestBody UserLoginRequestObject userLoginRequestObject) {
        var username = userLoginRequestObject.getUsername();
        //System.out.println(username);
        var userObject = userRepository.findByUserName(username);
        //System.out.println(userObject);
        var userAuthenticationObject = new UserAuthenticationObject();
        if (!userObject.isPresent() || userObject == null) {
            userAuthenticationObject.setUsername(userAuthenticationObject.getUsername());
            userAuthenticationObject.setPassword(null);
            userAuthenticationObject.setMessage("username does not exists in the DB");
            userAuthenticationObject.setAuthenticated(false);
            userAuthenticationObject.setToken(null);
        } else {
            var dbPassword = userObject.get().getUserPassword();
            var requestPassword = userLoginRequestObject.getPassword();
            System.out.println(dbPassword);
            if (!passwordEncoder.matches(requestPassword, dbPassword)) {
                userAuthenticationObject.setUsername(userLoginRequestObject.getUsername());
                userAuthenticationObject.setPassword(requestPassword);
                userAuthenticationObject.setMessage("password does not match");
                userAuthenticationObject.setAuthenticated(false);
                userAuthenticationObject.setToken(null);
            } else {

                userAuthenticationObject.setUsername(userLoginRequestObject.getUsername());
                userAuthenticationObject.setPassword(requestPassword);
                userAuthenticationObject.setMessage("User is Authenticated");
                userAuthenticationObject.setAuthenticated(true);
                userAuthenticationObject.setToken(authenticateAndGetToken(userLoginRequestObject));
            }
        }
        return userAuthenticationObject;
    }

//    @PostMapping("/generateToken")
//    public String authenticateAndGetToken(@RequestBody UserLoginRequestObject userLoginRequestObject) {
//        System.out.println("userLoginRequestObject" +userLoginRequestObject);
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestObject.getUsername(), userLoginRequestObject.getPassword()));
//        System.out.println("authentication" +authentication);
//        if (authentication.isAuthenticated()) {
//            return jwtService.generateToken(userLoginRequestObject.getUsername());
//        } else {
//            throw new UsernameNotFoundException("invalid user request !");
//        }
//    }
    public String authenticateAndGetToken(UserLoginRequestObject userLoginRequestObject) {
        //System.out.println(userLoginRequestObject);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestObject.getUsername(), userLoginRequestObject.getPassword()));
        //System.out.println(userLoginRequestObject);
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userLoginRequestObject.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @GetMapping("/get-name/{userId}")
    public ResponseEntity<?> findNameByUserid(String userId){
        String name = userRepository.findNameByUserId(userId);

        return new ResponseEntity<>(
                new CustomResponse(name, true), HttpStatus.OK);
    }

    @GetMapping("/logout/{id}")
    public ResponseEntity<?> logOutById(@PathVariable long id) {
        var authority = authorityRepository.findById(id);
        if (authority.isPresent()) {
            authority.get().setAuthority("");
            return new ResponseEntity<>("Logged out successfully", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("User not found", HttpStatus.ACCEPTED);
    }

    @PostMapping("/forgot-password/{userName}")
    public ResponseEntity<?> forgotPassword(@PathVariable String userName,@RequestParam String oldPassword, @RequestParam String newPassword) {
        var user = userRepository.findByUserName(userName);
        if (user.isPresent()) {
//            System.out.println(oldPassword +" " +  newPassword + " " + passwordEncoder.matches(user.get().getUserPassword(), oldPassword)   + " " + !(passwordEncoder.matches(user.get().getUserPassword(), newPassword)) );
            if(passwordEncoder.matches(oldPassword ,user.get().getUserPassword() ) && !(oldPassword.equals(newPassword)) && !(passwordEncoder.matches(newPassword , user.get().getUserPassword() ))) {
                user.get().setUserPassword(passwordEncoder.encode(newPassword));
                var updatedUser = userRepository.save(user.get());
                var userdata = userDataRepository.findByUserName(userName);
                userdata.get().setUserPassword(passwordEncoder.encode(newPassword));
                var updatedUserData = userDataRepository.save(userdata.get());
                return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
            }
            else if(oldPassword.equals(newPassword) || passwordEncoder.matches(newPassword , user.get().getUserPassword())){
                return new ResponseEntity<>("Do not enter the same password", HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Old Password Does not Match", HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>("User does not exist", HttpStatus.ACCEPTED);

    }

    @GetMapping("/get-all-users")
    public List<User> getAllUsers() {

        var user = userRepository.findAll();
        var displayedUser = new ArrayList<User>();
        for (User u : user) {
            var newuser = new User();
            newuser.setUserId(u.getUserId());
            newuser.setUserName(u.getUserName());
            newuser.setBlocked(u.isBlocked());
            newuser.setAdmin(u.isAdmin());
            displayedUser.add(newuser);
        }
        return displayedUser;
    }

    @GetMapping("/get-user-by-id/{userName}")
    public ResponseEntity<?> getUserById(@PathVariable String userName) {
        var optionalUserObject = userRepository.findByUserName(userName);
        if (!optionalUserObject.isPresent()) {
            throw new UserNotFoundException("User" + userName + " does not exist");

        }
        return new ResponseEntity<>(optionalUserObject.get(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-user-data-by-id/{userName}")
    public ResponseEntity<?> getUserDataById(@PathVariable String userName) {
        var optionalUserObject = userDataRepository.findByUserName(userName);
        if (!optionalUserObject.isPresent()) {
            throw new UserNotFoundException("User" + userName + " does not exist");

        }
        return new ResponseEntity<>(optionalUserObject, HttpStatus.ACCEPTED);
    }

}
