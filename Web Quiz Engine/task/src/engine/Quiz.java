package engine;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String title;
    private String text;
    @ElementCollection
    private List<String> options = new ArrayList<>();
    @ElementCollection
    private List<String> answer;

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

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    @JsonGetter
    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
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
}
