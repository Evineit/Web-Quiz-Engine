package engine.controllers;

import engine.models.User;
import engine.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;

@RestController
public class UserController {
    private static final String SERVICE_WARNING_MESSAGE = "Error";

    @Autowired
    private UserDetailsService userDetailsService;


    @PostMapping(value = "/api/register", consumes = "application/json")
    User newQuiz(@RequestBody User user) {
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
