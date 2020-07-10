package engine.controllers;

import engine.models.Quiz;
import engine.services.QuizService;
import engine.models.CompletionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.NoSuchElementException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class QuizController {
    private static final String SERVICE_WARNING_MESSAGE = "An error has occurred";

    @Autowired
    private QuizService quizService;
    public QuizController() {
    }

    @Secured("ROLE_USER")
    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<String> getQuizById(@PathVariable long id) {
        Quiz quiz;
        try {
            quiz = quizService.getQuizById((id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, SERVICE_WARNING_MESSAGE);
        }

        if (quiz != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                    .body(quiz.toString());
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/api/quizzes",produces = APPLICATION_JSON_VALUE)
    public Page<Quiz> getAllQuizzes(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        return quizService.getAllQuizzes(page, pageSize, sortBy);
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/api/quizzes/completed",produces = APPLICATION_JSON_VALUE)
    public Page<CompletionDto> getAllCompletions(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @Autowired Principal principal
            )
    {
        return quizService.getAllCompletions(page, pageSize, sortBy,principal);
    }

    @Secured("ROLE_USER")
    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    ResponseEntity<String> newQuiz(@RequestBody Quiz quiz,
                                   @Autowired Principal principal) {
        if (quiz.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERVICE_WARNING_MESSAGE);
        }
        if (quiz.getText().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERVICE_WARNING_MESSAGE);

        }
        if (quiz.getOptions().size()<2){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERVICE_WARNING_MESSAGE);

        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .body(addQuiz(quiz,principal));
    }

    @Secured("ROLE_USER")
    @PostMapping(value = "/api/quizzes/{id}/solve",consumes = "application/json")
    ResponseEntity<String> solveQuiz(@PathVariable int id,
                                     @RequestBody Quiz answer,
                                     @Autowired Principal principal) {
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                    .body(quizService.solve(id,answer.getAnswer(),principal.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound()
                    .build();
        }
    }
    @Secured("ROLE_USER")
    @DeleteMapping("/api/quizzes/{id}")
    ResponseEntity<String> deleteQuiz(@PathVariable long id,
                                      @Autowired Principal principal) {
        try {
            Quiz quiz = quizService.getQuizById(id);
            assert quiz!=null;
            quizService.deleteQuizById(id,quiz,principal);
            return ResponseEntity.noContent().build();
        }catch (NullPointerException |NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    private String addQuiz(Quiz quiz, Principal principal) {
        Quiz savedQuiz = quizService.saveQuiz(quiz, principal.getName());
        return savedQuiz.toString();
    }
}
