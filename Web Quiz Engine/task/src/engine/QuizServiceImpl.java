package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService{

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public List<Quiz> getAllQuizs() {
        return (List<Quiz>) quizRepository.findAll();
    }

    @Override
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).get();
    }

    @Override
    public Quiz saveQuiz(Quiz Quiz) {
        return quizRepository.save(Quiz);
    }

    @Override
    public Quiz updateQuizById(Long id, Quiz QuizToUpdate) {
        Quiz quizFromDb = quizRepository.findById(id).get();
        quizFromDb.setTitle(QuizToUpdate.getTitle());
        quizFromDb.setText(QuizToUpdate.getText());
        quizFromDb.setOptions(QuizToUpdate.getOptions());
        quizFromDb.setAnswer(QuizToUpdate.getAnswer());
        return quizRepository.save(quizFromDb);
    }

    @Override
    public void deleteQuizById(Long id) {
        quizRepository.deleteById(id);
    }

    @Override
    public List<Quiz> getQuizByTitleContaining(String searchString) {
        return quizRepository.findByTitleContaining(searchString);
    }

    @Override
    public List<Quiz> getQuizByTitleLike(String searchString) {
        return quizRepository.findByTitleLike(searchString);
    }
}
