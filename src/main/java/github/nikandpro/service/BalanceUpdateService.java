package github.nikandpro.service;

import github.nikandpro.entity.Account;
import github.nikandpro.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceUpdateService {
    private final AccountRepository accountRepository;

    private static final BigDecimal INCREASE_RATE = new BigDecimal("1.10");
    private static final BigDecimal MAX_MULTIPLIER = new BigDecimal("2.07");

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void updateAllBalances() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            BigDecimal currentBalance = account.getBalance();
            BigDecimal initialDeposit = account.getInitialDeposit();

            BigDecimal newBalance = currentBalance.multiply(INCREASE_RATE)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal maxAllowedBalance = initialDeposit.multiply(MAX_MULTIPLIER)
                    .setScale(2, RoundingMode.HALF_UP);

            if (newBalance.compareTo(maxAllowedBalance) > 0) {
                newBalance = maxAllowedBalance;
            }

            if (currentBalance.compareTo(newBalance) != 0) {
                account.setBalance(newBalance);
                accountRepository.save(account);
            }
        }
    }
}
