package engine;

import java.util.ArrayList;

public class Quiz {
    private String title;
    private String text;
    private ArrayList<String> options = new ArrayList<>();
    private int answer;

    public Quiz() {
    }

    public Quiz(String title, String text, ArrayList<String> options) {
        this.title = title;
        this.text = text;
        this.options = options;
    }

    public Quiz(String title, String text, ArrayList<String> options, int answer) {
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

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String solveQuiz(int answer) {
        if (answer == this.answer) {
            return "{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}";
        } else {
            return "{\"success\":false,\"feedback\":\"Wrong answer! Please, try again.\"}\n";
        }
    }

    public static String saveAnswer(int answer) {
        if (answer == 2) {
            return "{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}";
        } else {
            return "{\"success\":false,\"feedback\":\"Wrong answer! Please, try again.\"}\n";
        }
    }
}
