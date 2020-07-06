package engine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    List<Quiz> findByTitleContaining(String value);
    @Query("SELECT p FROM Quiz p WHERE p.title LIKE %:value%")
    List<Quiz> findByTitleLike(@Param("value") String value);
}
