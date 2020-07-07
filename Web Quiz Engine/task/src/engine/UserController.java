package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Set;

@RestController
public class UserController {
    private static final String SERVICE_WARNING_MESSAGE = "Error";

    @Autowired
    private UserDetailsService userDetailsService;


    @PostMapping(value = "/api/register", consumes = "application/json")
    User newQuiz(@RequestBody User user) {
//        if (user.getEmail().isBlank()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERVICE_WARNING_MESSAGE);
//        }
//        if (user.getPassword().isBlank()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERVICE_WARNING_MESSAGE);
//
//        }
//        userDetailsService.saveUser(user);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
//                .body(createUserWithRole(user));
        return createUserWithRole(user);
    }
    private User createUserWithRole(User user) throws ConstraintViolationException {
        return (User) userDetailsService.saveUser(user);

    }
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = SERVICE_WARNING_MESSAGE)
    public HashMap<String, String> handleIndexOutOfBoundsException(Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", SERVICE_WARNING_MESSAGE);
        response.put("error", e.getClass().getSimpleName());
        return response;
    }
}
