package github.nikandpro.service;

import github.nikandpro.entity.Account;
import github.nikandpro.entity.User;
import github.nikandpro.repository.AccountRepository;
import github.nikandpro.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public void createAccountForUser(User user) {
        validateUser(user);

        Account account = new Account();
        account.setUser(user);
        account.setInitialDeposit(account.getBalance());
        accountRepository.save(account);
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
    }
}
