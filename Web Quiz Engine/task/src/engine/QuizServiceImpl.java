package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService{

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public List<Quiz> getAllQuizzes() {
        return (List<Quiz>) quizRepository.findAll();
    }

    @Override
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).get();
    }

    @Override
    public Quiz saveQuiz(Quiz Quiz) {
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
        }
        Quiz.setAuthor(username);
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
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
        }
        Quiz quizFromDb = quizRepository.findById(id).get();
        if (!quizFromDb.getAuthor().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

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
