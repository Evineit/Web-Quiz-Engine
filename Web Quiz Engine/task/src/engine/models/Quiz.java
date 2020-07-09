package engine.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String text;
    @ElementCollection
    private List<String> options = new ArrayList<>();
    @ElementCollection
    private List<String> answer;
    private String author;
    public Quiz() {
    }

    public Quiz(String title, String text, ArrayList<String> options) {
        this.title = title;
        this.text = text;
        this.options = options;
    }

    public Quiz(String title, String text, ArrayList<String> options, List<String> answer) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonGetter
    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @JsonGetter
    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String solveQuiz(List<String> answer) {
        if (this.answer==null && answer==null){
            return "{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}";
        }else if (this.answer == null && answer.isEmpty()) {
            return "{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}";
        }else if (answer == null && this.answer.isEmpty()) {
            return "{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}";
        }else if (answer.equals(this.answer)) {
            return "{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}";
        }else {
            return "{\"success\":false,\"feedback\":\"Wrong answer! Please, try again.\"}\n";
        }
    }

    public static String saveAnswer(Integer[] answer) {
        List<Integer> list = Arrays.asList(answer);
        if (list.contains(2)) {
            return "{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}";
        } else {
            return "{\"success\":false,\"feedback\":\"Wrong answer! Please, try again.\"}\n";
        }
    }

    @Override
    public String toString() {
        String result = null;
        try {
            result = new ObjectMapper().writeValueAsString(getOptions());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"title\": \"" + getTitle() + "\",\n" +
                "  \"text\": \"" + getText() + "\",\n" +
                "  \"options\": " + result + "\n" +
                "}";
    }
}
