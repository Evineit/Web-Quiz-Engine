package engine;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    private Quiz quiz;

    public Controller() {
    }

    @GetMapping("/api/quiz")
    public ResponseEntity<String> getQuiz(){
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .body("{\n" +
                        "  \"title\": \"The Java Logo\",\n" +
                        "  \"text\": \"What is depicted on the Java logo?\",\n" +
                        "  \"options\": [\"Robot\",\"Tea leaf\",\"Cup of coffee\",\"Bug\"]\n" +
                        "}\n");
    }

    @PostMapping("/api/quiz")
    ResponseEntity<String> answerQuiz(@RequestParam("answer") int answer) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .body(quiz.saveAnswer(answer));
    }
}
