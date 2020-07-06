package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {
    private static final String SERVICE_WARNING_MESSAGE = "Error";

    @Autowired
    private UserDetailsService userDetailsService;


    @PostMapping(value = "/api/register", consumes = "application/json")
    ResponseEntity<String> newQuiz(@RequestBody User user) {
        if (user.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERVICE_WARNING_MESSAGE);
        }
        if (user.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERVICE_WARNING_MESSAGE);

        }
//        userDetailsService.saveUser(user);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .body(createUserWithRole(user));
    }
    private String createUserWithRole(User user){
        if(userDetailsService.loadUserByUsername(user.getUsername())==null){
            userDetailsService.saveUser(user);
        }
        return "ok";
    }
}
