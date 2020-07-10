package engine;

import engine.models.Completion;
import engine.models.Quiz;
import engine.models.CompletionDto;
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

import java.security.Principal;
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
        final Iterable<Quiz> all = quizRepository.findAll();
        List<Quiz> target = new ArrayList<>();
        all.forEach(target::add);
        return target;
    }

    @Override
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).get();
    }

    @Override
    public Quiz saveQuiz(Quiz Quiz) {
        Quiz newQuiz = new Quiz();
        newQuiz.setTitle(Quiz.getTitle());
        newQuiz.setText(Quiz.getText());
        newQuiz.setOptions(Quiz.getOptions());
        newQuiz.setAnswer(Quiz.getAnswer());
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
        }
        newQuiz.setAuthor(username);
        return quizRepository.save(newQuiz);
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

        quizRepository.delete(quizFromDb);
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
    public Page<Completion> getCompletions(Pageable pageable) {
        return quizRepository.findCompletions(pageable);
    }

    @Override
    public Page<Quiz> getAllQuizzes(Integer page, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        Page<Quiz> pagedResult = quizRepository.findAll(paging);
        Page<Quiz> res = pagedResult.map(QuizServiceImpl::convertQuizEntityToDtoWithoutAnswer);
        return res;

    }

    @Override
    public Page<CompletionDto> getAllCompletions(Integer page, Integer pageSize, String sortBy, Principal principal) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        Page<Completion> pagedResult = completionRepository.findAllByUserOrderByCompletedAtDesc(principal.getName(),paging);
        Page<CompletionDto> res = pagedResult.map(QuizServiceImpl::convertCompletionEntityToDto);
        return res;
    }
    public static CompletionDto convertCompletionEntityToDto(Completion completion) {
        CompletionDto completionDto = new CompletionDto();
        completionDto.setId(completion.getQuiz().getId());
        completionDto.setQuizTitle(completion.getQuiz().getTitle());
        completionDto.setCompletedAt(completion.getCompletedAt());
        return completionDto;
    }
    public static Quiz convertQuizEntityToDtoWithoutAnswer(Quiz quiz) {
        Quiz newQuiz = new Quiz();
        newQuiz.setId(quiz.getId());
        newQuiz.setTitle(quiz.getTitle());
        newQuiz.setText(quiz.getText());
        newQuiz.setOptions(new ArrayList<>(quiz.getOptions()));
        return newQuiz;
    }
}
