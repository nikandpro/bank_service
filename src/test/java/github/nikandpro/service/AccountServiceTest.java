package github.nikandpro.service;

import github.nikandpro.entity.Account;
import github.nikandpro.entity.User;
import github.nikandpro.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountForUser_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Account savedAccount = new Account();
        savedAccount.setBalance(BigDecimal.valueOf(100));

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account arg = invocation.getArgument(0);
            arg.setId(1L);
            return arg;
        });

        // Act
        accountService.createAccountForUser(user);

        // Assert
        verify(accountRepository).save(any(Account.class));
    }


    @Test
    void createAccountForUser_VerifyRepositoryCall() {
        // Arrange
        User user = new User();
        Account expectedAccount = new Account();

        when(accountRepository.save(any())).thenReturn(expectedAccount);

        // Act
        accountService.createAccountForUser(user);

        // Assert
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        Account savedAccount = captor.getValue();
        assertSame(user, savedAccount.getUser());
    }

    @Test
    void createAccountForUser_NullUser_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                accountService.createAccountForUser(null)
        );
    }
}
