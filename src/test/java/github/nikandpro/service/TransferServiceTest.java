package github.nikandpro.service;

import github.nikandpro.dto.request.TransferRequest;
import github.nikandpro.entity.Account;
import github.nikandpro.exception.BadRequestException;
import github.nikandpro.exception.InsufficientFundsException;
import github.nikandpro.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    void transferMoney_Success() {
        // Arrange
        Long senderId = 1L;
        Long recipientId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        Account senderAccount = new Account();
        senderAccount.setBalance(new BigDecimal("200.00"));
        senderAccount.setId(senderId);

        Account recipientAccount = new Account();
        recipientAccount.setBalance(new BigDecimal("50.00"));
        recipientAccount.setId(recipientId);

        when(accountRepository.findByUserIdWithLock(senderId))
                .thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserIdWithLock(recipientId))
                .thenReturn(Optional.of(recipientAccount));

        TransferRequest request = new TransferRequest(recipientId, amount);

        // Act
        transferService.transferMoney(senderId, request);

        // Assert
        assertEquals(new BigDecimal("100.00"), senderAccount.getBalance());
        assertEquals(new BigDecimal("150.00"), recipientAccount.getBalance());
    }

    @Test
    void transferMoney_TransferToSelf_ThrowsException() {
        // Arrange
        Long userId = 1L;
        TransferRequest request = new TransferRequest(userId, new BigDecimal("100.00"));

        // Act & Assert
        assertThrows(BadRequestException.class, () ->
                transferService.transferMoney(userId, request)
        );
    }

    @Test
    void transferMoney_InsufficientFunds_ThrowsException() {
        // Arrange
        Long senderId = 1L;
        Long recipientId = 2L;
        BigDecimal amount = new BigDecimal("200.00");

        Account senderAccount = new Account();
        senderAccount.setBalance(new BigDecimal("100.00"));
        senderAccount.setId(senderId);

        Account recipientAccount = new Account();
        recipientAccount.setId(recipientId);

        when(accountRepository.findByUserIdWithLock(senderId))
                .thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserIdWithLock(recipientId))
                .thenReturn(Optional.of(recipientAccount));

        TransferRequest request = new TransferRequest(recipientId, amount);

        // Act & Assert
        assertThrows(InsufficientFundsException.class, () ->
                transferService.transferMoney(senderId, request)
        );
    }

    @Test
    void transferMoney_SenderNotFound_ThrowsException() {
        // Arrange
        Long senderId = 1L;
        Long recipientId = 2L;

        when(accountRepository.findByUserIdWithLock(senderId))
                .thenReturn(Optional.empty());

        TransferRequest request = new TransferRequest(recipientId, new BigDecimal("100.00"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                transferService.transferMoney(senderId, request)
        );
    }

    @Test
    void transferMoney_RecipientNotFound_ThrowsException() {
        // Arrange
        Long senderId = 1L;
        Long recipientId = 2L;

        Account senderAccount = new Account();
        senderAccount.setId(senderId);

        when(accountRepository.findByUserIdWithLock(senderId))
                .thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserIdWithLock(recipientId))
                .thenReturn(Optional.empty());

        TransferRequest request = new TransferRequest(recipientId, new BigDecimal("100.00"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                transferService.transferMoney(senderId, request)
        );
    }

    @Test
    void transferMoney_AccountsAreLocked() {
        // Arrange
        Long senderId = 1L;
        Long recipientId = 2L;
        BigDecimal amount = new BigDecimal("50.00");

        Account senderAccount = new Account();
        senderAccount.setBalance(new BigDecimal("100.00"));
        senderAccount.setId(senderId);

        Account recipientAccount = new Account();
        recipientAccount.setId(recipientId);

        when(accountRepository.findByUserIdWithLock(senderId))
                .thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserIdWithLock(recipientId))
                .thenReturn(Optional.of(recipientAccount));

        TransferRequest request = new TransferRequest(recipientId, amount);

        // Act
        transferService.transferMoney(senderId, request);

        // Assert
        verify(accountRepository).findByUserIdWithLock(senderId);
        verify(accountRepository).findByUserIdWithLock(recipientId);
    }


}
