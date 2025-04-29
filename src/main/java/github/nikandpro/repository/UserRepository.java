package github.nikandpro.repository;

import github.nikandpro.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.dateOfBirth > :dateOfBirth")
    Page<User> findByDateOfBirthAfter(
            @Param("dateOfBirth") LocalDate dateOfBirth,
            PageRequest pageable
    );

    @Query("SELECT u FROM User u JOIN u.phones p WHERE p.phone = :phone")
    User findByPhone(@Param("phone") String phone);

    @Query("SELECT u FROM User u JOIN u.emails e WHERE e.email = :email")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.name LIKE CONCAT(:name, '%')")
    Page<User> findByNameStartingWith(
            @Param("name") String name,
            PageRequest pageable
    );
}
