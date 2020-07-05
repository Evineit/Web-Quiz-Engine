package engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class Controller {
    private ArrayList<Quiz> quizzes = new ArrayList<>();

    public Controller() {
    }

    @GetMapping("/api/quiz")
    public ResponseEntity<String> getQuiz() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .body("{\n" +
                        "  \"title\": \"The Java Logo\",\n" +
                        "  \"text\": \"What is depicted on the Java logo?\",\n" +
                        "  \"options\": [\"Robot\",\"Tea leaf\",\"Cup of coffee\",\"Bug\"]\n" +
                        "}\n");
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<String> getQuizById(@PathVariable int id) {
        Quiz quiz = null;
        try {
            quiz = quizzes.get(id - 1);
        } catch (Exception e) {
            return ResponseEntity.notFound()
                    .build();
        }
        if (quiz != null) {
            String result = null;
            try {
                result = new ObjectMapper().writeValueAsString(quiz.getOptions());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                    .body("{\n" +
                            "  \"id\": " + id + ",\n" +
                            "  \"title\": \"" + quiz.getTitle() + "\",\n" +
                            "  \"text\": \"" + quiz.getText() + "\",\n" +
                            "  \"options\": " + result + "\n" +
                            "}");
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @GetMapping("/api/quizzes")
    public ResponseEntity<String> getQuizzes() {
        String[] responseBody = new String[quizzes.size()];
        for (int i = 0; i < quizzes.size(); i++) {
            Quiz quiz = quizzes.get(i);
            String result = null;
            try {
                result = new ObjectMapper().writeValueAsString(quiz.getOptions());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            responseBody[i] = "{\n" +
                    "  \"id\": " + i + 1 + ",\n" +
                    "  \"title\": \"" + quiz.getTitle() + "\",\n" +
                    "  \"text\": \"" + quiz.getText() + "\",\n" +
                    "  \"options\": " + result + "\n" +
                    "}";
        }
        System.out.println(Arrays.toString(responseBody));
        if (quizzes.isEmpty()){
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                    .body("[]");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .body(Arrays.toString(responseBody));
    }

    @PostMapping("/api/quiz")
    ResponseEntity<String> answerQuiz(@RequestParam("answer") int answer) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .body(Quiz.saveAnswer(answer));
    }

    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    ResponseEntity<String> newQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .body(addQuiz(quiz));
    }

    @PostMapping(value = "/api/quizzes/{id}/solve")
    ResponseEntity<String> solveQuiz(@PathVariable int id, @RequestParam int answer) {
        try {
            Quiz quiz = quizzes.get(id-1);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                    .body(quiz.solveQuiz(answer));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound()
                .build();
    }

    private String addQuiz(Quiz quiz) {
        quizzes.add(quiz);
        String result = null;
        try {
            result = new ObjectMapper().writeValueAsString(quiz.getOptions());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{\n" +
                "  \"id\": " + quizzes.size() + ",\n" +
                "  \"title\": \"" + quiz.getTitle() + "\",\n" +
                "  \"text\": \"" + quiz.getText() + "\",\n" +
                "  \"options\": " + result + "\n" +
                "}";
    }
}
