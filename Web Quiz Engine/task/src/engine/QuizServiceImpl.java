package engine;

import engine.models.Quiz;
import engine.repositories.CompletionRepository;
import engine.repositories.QuizRepository;
import engine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static engine.models.Completion.createCompletion;

@Service
public class QuizServiceImpl implements QuizService{

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private CompletionRepository completionRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
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
    public String solve(long quizId, List<String> answer, String username) {
        var user = userRepository.findByEmail(username);
        var quiz = getQuizById(quizId);
        if (quiz.getAnswer()==null && answer==null){
            completionRepository.save(createCompletion(user, quiz));
            return "{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}";
        }else if (quiz.getAnswer() == null && answer.isEmpty()) {
            completionRepository.save(createCompletion(user, quiz));
            return "{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}";
        }else if (answer == null && quiz.getAnswer().isEmpty()) {
            completionRepository.save(createCompletion(user, quiz));
            return "{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}";
        }else if (answer.equals(quiz.getAnswer())) {
            completionRepository.save(createCompletion(user, quiz));
            return "{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}";
        }else {
            return "{\"success\":false,\"feedback\":\"Wrong answer! Please, try again.\"}\n";
        }
    }

    @Override
    public List<Quiz> getQuizByTitleLike(String searchString) {
        return quizRepository.findByTitleLike(searchString);
    }

    @Override
    public Page<Quiz> getCompletions(Pageable pageable) {
        return quizRepository.findCompletions(pageable);
    }

    @Override
    public List<Quiz> getAllQuizzes(Integer page, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        Page<Quiz> pagedResult = quizRepository.findAll(paging);
        Page<Quiz> res = pagedResult.map(QuizServiceImpl::convertQuizEntityToDtoWithoutAnswer);
        if(pagedResult.hasContent()) {
            return res.getContent();
        } else {
            return new ArrayList<Quiz>();
        }
    }

    public static Quiz convertQuizEntityToDtoWithoutAnswer(Quiz quiz) {
        Quiz newQuiz = new Quiz();
        newQuiz.setId(quiz.getId());
        newQuiz.setTitle(quiz.getTitle());
        newQuiz.setText(quiz.getText());
        newQuiz.setOptions(new ArrayList<>(quiz.getOptions()));
        return newQuiz;
    }

    @Override
    public List<Quiz> getAllCompletions(Integer page, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));

        Page<Quiz> pagedResult = quizRepository.findCompletions(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Quiz>();
        }
    }
}
