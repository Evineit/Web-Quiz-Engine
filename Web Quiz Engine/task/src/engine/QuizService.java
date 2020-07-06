package engine;

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
}
