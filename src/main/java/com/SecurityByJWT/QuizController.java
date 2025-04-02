package com.SecurityByJWT;




import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class QuizController {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    UserRepo userRepo;
    String jwtToken;
    @Autowired
    JwtService jwtService;
    @GetMapping("/home")
    public String startQuiz(HttpServletRequest request) {
        return "Quiz session started" +request.getSession().getId();
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletResponse response){
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            System.out.println("1st hi");
              jwtToken=jwtService.generateToken(user.getUsername()) ;
            System.out.println("hii");
            response.addCookie(createJwtCookie(jwtToken));
            System.out.println("lat hii");
            return "LOgin sucessfull  "+ jwtToken;
        } else {
            return "fail";
        }
    }

    private Cookie createJwtCookie(String jwt) {
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true); // Prevent JavaScript access for security
        jwtCookie.setSecure(true); // Use HTTPS for the cookie
        jwtCookie.setPath("/"); // Make cookie available site-wide
        jwtCookie.setMaxAge(60 * 60 * 24); // Token expiration (1 day in seconds)
        return jwtCookie;
    }
}
