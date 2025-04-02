package com.SecurityByJWT;



import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class UserController {
    @Autowired
    UserRepo userRepo;


    @GetMapping("/users")
    public List<User>getUsers(){
        return this.userRepo.findAll();
    }
    @PostMapping("/users")
    public User createUser(@RequestBody User user){
        return this.userRepo.save(user);
    }

    @GetMapping("users/{id}")
    public User getCsrfToken(@PathVariable Integer id){
        return userRepo.findById(id).orElseThrow(()-> new RuntimeException("user not found"));
    }
}
