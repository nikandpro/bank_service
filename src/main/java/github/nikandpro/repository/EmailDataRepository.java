package github.nikandpro.repository;

import github.nikandpro.entity.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
    boolean existsByEmailAndUserIdNot(String email, Long userId);
}
