package github.nikandpro.service;

import github.nikandpro.dto.EmailDataDto;
import github.nikandpro.entity.EmailData;
import github.nikandpro.entity.User;
import github.nikandpro.exception.BadRequestException;
import github.nikandpro.exception.ConflictException;
import github.nikandpro.mapper.EmailMapper;
import github.nikandpro.repository.EmailDataRepository;
import github.nikandpro.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailDataService {
    private final EmailDataRepository emailDataRepository;
    private final EmailMapper emailMapper;

    public EmailDataDto addEmailToUser(User user, String email) {
        if (emailDataRepository.existsByEmail(email)) {
            throw new ConflictException("Email already in use");
        }

        EmailData emailData = new EmailData();
        emailData.setEmail(email);
        emailData.setUser(user);

        return emailMapper.toDto(emailDataRepository.save(emailData));
    }

    public void removeEmailFromUser(Long userId, Long emailId) {
        EmailData emailData = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new EntityNotFoundException("Email not found"));

        validateEmailOwnership(userId, emailData);
        validateNotLastEmail(emailData);

        emailDataRepository.delete(emailData);
    }

    private void validateEmailOwnership(Long userId, EmailData emailData) {
        if (!emailData.getUser().getId().equals(userId)) {
            throw new BadRequestException("Email does not belong to user");
        }
    }

    private void validateNotLastEmail(EmailData emailData) {
        if (emailData.getUser().getEmails().size() <= 1) {
            throw new BadRequestException("Cannot delete the last email");
        }
    }
}