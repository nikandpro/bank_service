package github.nikandpro.repository;

import github.nikandpro.entity.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
    boolean existsByPhone(String phone);
}
