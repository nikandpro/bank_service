package github.nikandpro.service;

import github.nikandpro.dto.request.TransferRequest;
import github.nikandpro.entity.Account;
import github.nikandpro.exception.BadRequestException;
import github.nikandpro.exception.InsufficientFundsException;
import github.nikandpro.repository.AccountRepository;
import github.nikandpro.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountRepository accountRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 30)
    public void transferMoney(Long senderUserId, TransferRequest request) {
        if (senderUserId.equals(request.getRecipientId())) {
            throw new BadRequestException("Cannot transfer to yourself");
        }

        Account senderAccount = accountRepository.findByUserIdWithLock(senderUserId)
                .orElseThrow(() -> new EntityNotFoundException("Sender account not found"));

        Account recipientAccount = accountRepository.findByUserIdWithLock(request.getRecipientId())
                .orElseThrow(() -> new EntityNotFoundException("Recipient account not found"));

        if (senderAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Not enough balance");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(request.getAmount()));
        recipientAccount.setBalance(recipientAccount.getBalance().add(request.getAmount()));

    }
}
