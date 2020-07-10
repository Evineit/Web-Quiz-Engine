package engine.repositories;

import engine.models.Completion;
import engine.models.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends PagingAndSortingRepository<Quiz,Long> {
    List<Quiz> findByTitleContaining(String value);

    @Query("SELECT p FROM Quiz p WHERE p.title LIKE %:value%")
    List<Quiz> findByTitleLike(@Param("value") String value);

    @Query("SELECT p FROM Completion p")
    Page<Completion> findCompletions(Pageable pageable);



}
