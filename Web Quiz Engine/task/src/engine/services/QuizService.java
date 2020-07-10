package engine.services;

import engine.models.Completion;
import engine.models.Quiz;
import engine.models.CompletionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface QuizService {
    /**
     * Get All Quizzes.
     * @return List of all planets.
     */
    List<Quiz> getAllQuizzes();
    /**
     * Get Quiz By Id.
     * @param id Id
     * @return Quiz
     */
    Quiz getQuizById(Long id);
    /**
     * Save Quiz.
     * @param Quiz Quiz to save
     * @return Saved Quiz
     */
    Quiz saveQuiz(Quiz Quiz);
    /**
     * Update Quiz.
     * @param id Id
     * @param QuizToUpdate Quiz to Update
     * @return Updated Quiz
     */
    Quiz updateQuizById(Long id, Quiz QuizToUpdate);
    /**
     * Delete Quiz by Id.
     * @param id Id
     */
    void deleteQuizById(Long id);
    public String solve(long quizId, List<String> answer, String username);
    /**
     * Search Quiz by Title containing.
     * @param searchString SearchString
     * @return Search result
     */
    List<Quiz> getQuizByTitleContaining(String searchString);
    /**
     * Search Quiz by Title like.
     * @param searchString SearchString
     * @return Search result
     */
    List<Quiz> getQuizByTitleLike(String searchString);

    Page<Completion> getCompletions(Pageable pageable);

    Page<Quiz> getAllQuizzes(Integer pageNo, Integer pageSize, String sortBy);

    Page<CompletionDto> getAllCompletions(Integer page, Integer pageSize, String sortBy, Principal principal);
}
