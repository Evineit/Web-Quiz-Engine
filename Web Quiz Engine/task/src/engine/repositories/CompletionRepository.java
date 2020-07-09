package engine.repositories;

import engine.models.Completion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletionRepository extends JpaRepository<Completion, Long> {

    @Query("SELECT c FROM Completion c where c.user.email = :username order by c.completedAt desc")
    Page<Completion> findAllByUserOrderByCompletedAtDesc(String username, Pageable pageable);
}
