package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class QuizController {
    private static final String SERVICE_WARNING_MESSAGE = "An error has occurred";

    @Autowired
    private QuizService quizService;
    public QuizController() {
    }

//    @GetMapping("/api/quiz")
//    public ResponseEntity<String> getQuiz() {
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
//                .body("{\n" +
//                        "  \"title\": \"The Java Logo\",\n" +
//                        "  \"text\": \"What is depicted on the Java logo?\",\n" +
//                        "  \"options\": [\"Robot\",\"Tea leaf\",\"Cup of coffee\",\"Bug\"]\n" +
//                        "}\n");
//    }
    @Secured("ROLE_USER")
//    @PreAuthorize("hasRole('USER')")
    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<String> getQuizById(@PathVariable int id) {
        Quiz quiz;
        try {
            quiz = quizService.getQuizById((long) (id));
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
    @GetMapping("/api/quizzes")
    public ResponseEntity<String> getQuizzes() {
        List<Quiz> quizList = quizService.getAllQuizzes();
        String[] responseBody = new String[quizList.size()];
        for (int i = 0; i < quizList.size(); i++) {
            responseBody[i]= quizList.get(i).toString();
        }
        System.out.println(Arrays.toString(responseBody));
        if (quizList.isEmpty()){
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                    .body("[]");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .body(Arrays.toString(responseBody));
    }

//    @PostMapping(value = "/api/quiz",consumes = "application/json")
//    ResponseEntity<String> answerQuiz(@RequestBody Integer[] answer) {
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
//                .body(Quiz.saveAnswer(answer));
//    }

    @Secured("ROLE_USER")
    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    ResponseEntity<String> newQuiz(@RequestBody Quiz quiz) {
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
                .body(addQuiz(quiz));
    }

    @Secured("ROLE_USER")
    @PostMapping(value = "/api/quizzes/{id}/solve",consumes = "application/json")
    ResponseEntity<String> solveQuiz(@PathVariable int id, @RequestBody Quiz answer) {
        try {
            Quiz quiz = quizService.getQuizById((long) (id));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                    .body(quiz.solveQuiz(answer.getAnswer()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound()
                    .build();
        }
    }
    @Secured("ROLE_USER")
    @DeleteMapping("/api/quizzes/{id}")
    ResponseEntity<String> deleteQuiz(@PathVariable int id) {
        try {
            Quiz quiz = quizService.getQuizById((long) (id));
            assert quiz!=null;
            quizService.deleteQuizById((long) id);
            return ResponseEntity.noContent()
                    .build();
        } catch (NullPointerException |NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.notFound()
                    .build();
        }
    }

    private String addQuiz(Quiz quiz) {
        Quiz savedQuiz = quizService.saveQuiz(quiz);
        return savedQuiz.toString();
    }
}
