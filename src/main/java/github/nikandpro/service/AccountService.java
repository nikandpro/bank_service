package github.nikandpro.service;

import github.nikandpro.entity.Account;
import github.nikandpro.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public void createAccount(Account account) {
        accountRepository.save(account);
    }
}
