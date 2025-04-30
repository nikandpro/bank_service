package github.nikandpro.service;

import github.nikandpro.entity.Account;
import github.nikandpro.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceUpdateServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BalanceUpdateService balanceUpdateService;

    @Test
    void updateAllBalances_SuccessfulUpdate() {
        // Arrange
        Account account = new Account();
        account.setInitialDeposit(new BigDecimal("100.00"));
        account.setBalance(new BigDecimal("100.00"));

        when(accountRepository.findAll()).thenReturn(List.of(account));

        // Act
        balanceUpdateService.updateAllBalances();

        // Assert
        assertEquals(new BigDecimal("110.00"), account.getBalance());
        verify(accountRepository).save(account);
    }

    @Test
    void updateAllBalances_ReachesMaxBalance() {
        // Arrange
        Account account = new Account();
        account.setInitialDeposit(new BigDecimal("100.00"));
        account.setBalance(new BigDecimal("200.00"));

        when(accountRepository.findAll()).thenReturn(List.of(account));

        // Act
        balanceUpdateService.updateAllBalances();

        // Assert
        assertEquals(new BigDecimal("207.00"), account.getBalance());
    }

    @Test
    void updateAllBalances_NoUpdateNeeded() {
        // Arrange
        Account account = new Account();
        account.setInitialDeposit(new BigDecimal("100.00"));
        account.setBalance(new BigDecimal("207.00"));

        when(accountRepository.findAll()).thenReturn(List.of(account));

        // Act
        balanceUpdateService.updateAllBalances();

        // Assert
        assertEquals(new BigDecimal("207.00"), account.getBalance());
        verify(accountRepository, never()).save(account);
    }

    @Test
    void updateAllBalances_RoundsCorrectly() {
        // Arrange
        Account account = new Account();
        account.setInitialDeposit(new BigDecimal("100.00"));
        account.setBalance(new BigDecimal("123.45"));

        when(accountRepository.findAll()).thenReturn(List.of(account));

        // Act
        balanceUpdateService.updateAllBalances();

        // Assert
        assertEquals(new BigDecimal("135.80"), account.getBalance());
    }

    @Test
    void updateAllBalances_MultipleAccounts() {
        // Arrange
        Account account1 = new Account();
        account1.setId(1L);
        account1.setInitialDeposit(new BigDecimal("100.00"));
        account1.setBalance(new BigDecimal("100.00"));

        Account account2 = new Account();
        account2.setId(2L);
        account2.setInitialDeposit(new BigDecimal("200.00"));
        account2.setBalance(new BigDecimal("150.00"));

        when(accountRepository.findAll()).thenReturn(List.of(account1, account2));

        // Act
        balanceUpdateService.updateAllBalances();

        // Assert
        assertEquals(new BigDecimal("110.00"), account1.getBalance());
        assertEquals(new BigDecimal("165.00"), account2.getBalance());

        verify(accountRepository).save(account1);
        verify(accountRepository).save(account2);
        verify(accountRepository, never()).saveAll(any());
    }

    @Test
    void updateAllBalances_BoundaryConditions() {
        // Arrange
        Account account = new Account();
        account.setInitialDeposit(new BigDecimal("100.00"));
        account.setBalance(new BigDecimal("206.99"));

        when(accountRepository.findAll()).thenReturn(List.of(account));

        // Act
        balanceUpdateService.updateAllBalances();

        // Assert
        assertEquals(new BigDecimal("207.00"), account.getBalance());
    }

    @Test
    void updateAllBalances_EmptyAccountList() {
        // Arrange
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        balanceUpdateService.updateAllBalances();

        // Assert
        verify(accountRepository, never()).save(any());
    }
}
