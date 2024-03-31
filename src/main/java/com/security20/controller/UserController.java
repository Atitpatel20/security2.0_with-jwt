package com.security20.controller;

import com.security20.dto.JwtResponse;
import com.security20.dto.LoginDto;
import com.security20.dto.UserDto;
import com.security20.service.UserRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserRegistrationService userRegistrationService;

    public UserController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    // http://localhost:8080/api/v1/users/register

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) throws Exception {
        UserDto dto = userRegistrationService.registerUser(userDto);
        if (dto != null) {
            return new ResponseEntity<>("user registration successful", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("user registration failed ,please try again", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // http://localhost:8080/api/v1/users/login
//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto){
//       String status= userRegistrationService.verifyLogin(loginDto);
//    if(status){
//        return new ResponseEntity<>("user sign in successful", HttpStatus.OK);
//    }
//        return new ResponseEntity<>("please enter correct credentials", HttpStatus.UNAUTHORIZED);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        String jwtToken = userRegistrationService.verifyLogin(loginDto);
        if (jwtToken != null) {
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setToken(jwtToken);
            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>("please enter correct credentials", HttpStatus.UNAUTHORIZED);
    }
}
