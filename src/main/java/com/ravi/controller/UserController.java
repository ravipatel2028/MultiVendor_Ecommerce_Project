package com.ravi.controller;


import com.ravi.entities.User;
import com.ravi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> profileHandler(@RequestHeader("Authorization") String token) {
        User user=userService.findUserByJwtToken(token);

        return ResponseEntity.ok(user);
    }
}
