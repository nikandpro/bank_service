package github.nikandpro.service;

import github.nikandpro.dto.EmailDataDto;
import github.nikandpro.entity.EmailData;
import github.nikandpro.entity.User;
import github.nikandpro.exception.BadRequestException;
import github.nikandpro.exception.ConflictException;
import github.nikandpro.mapper.EmailMapper;
import github.nikandpro.repository.EmailDataRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailDataServiceTest {

    @Mock
    private EmailDataRepository emailDataRepository;
    @Mock
    private EmailMapper emailMapper;

    @InjectMocks
    private EmailDataService emailDataService;

    @Test
    void addEmailToUser_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);
        String email = "test@example.com";
        EmailData savedEmail = new EmailData();
        EmailDataDto expectedDto = new EmailDataDto();

        when(emailDataRepository.existsByEmail(email)).thenReturn(false);
        when(emailDataRepository.save(any())).thenReturn(savedEmail);
        when(emailMapper.toDto(savedEmail)).thenReturn(expectedDto);

        // Act
        EmailDataDto result = emailDataService.addEmailToUser(user, email);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(emailDataRepository).save(argThat(e ->
                e.getEmail().equals(email) && e.getUser().equals(user)
        ));
    }

    @Test
    void addEmailToUser_EmailExists_ThrowsConflictException() {
        // Arrange
        String email = "existing@example.com";
        when(emailDataRepository.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictException.class, () ->
                emailDataService.addEmailToUser(new User(), email)
        );
    }

    @Test
    void removeEmailFromUser_Success() {
        // Arrange
        Long userId = 1L;
        Long emailId = 1L;

        User user = new User();
        user.setId(userId);

        EmailData email1 = new EmailData();
        email1.setId(emailId);
        email1.setUser(user);

        EmailData email2 = new EmailData();
        email2.setId(2L);
        email2.setUser(user);

        user.setEmails(new HashSet<>(Set.of(email1, email2)));

        when(emailDataRepository.findById(emailId)).thenReturn(Optional.of(email1));

        // Act
        emailDataService.removeEmailFromUser(userId, emailId);

        // Assert
        verify(emailDataRepository).delete(email1);
    }

    @Test
    void removeEmailFromUser_EmailNotFound_ThrowsException() {
        // Arrange
        when(emailDataRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                emailDataService.removeEmailFromUser(1L, 1L)
        );
    }

    @Test
    void validateNotLastEmail_MultipleEmails_NoException() {
        // Arrange
        User user = new User();
        user.setId(1L);

        EmailData email1 = new EmailData();
        email1.setId(1L);
        email1.setEmail("email1@test.com");

        EmailData email2 = new EmailData();
        email2.setId(2L);
        email2.setEmail("email2@test.com");


        email1.setUser(user);
        email2.setUser(user);
        user.setEmails(new HashSet<>(Arrays.asList(email1, email2)));

        when(emailDataRepository.findById(anyLong())).thenReturn(Optional.of(email1));

        // Act & Assert
        assertDoesNotThrow(() ->
                emailDataService.removeEmailFromUser(1L, 1L)
        );
    }

    @Test
    void validateNotLastEmail_LastEmail_ThrowsException() {
        // Arrange
        User user = new User();
        user.setId(1L);
        EmailData emailData = new EmailData();
        emailData.setUser(user);
        user.setEmails(Set.of(emailData));

        when(emailDataRepository.findById(anyLong())).thenReturn(Optional.of(emailData));

        // Act & Assert
        assertThrows(BadRequestException.class, () ->
                emailDataService.removeEmailFromUser(1L, 1L)
        );
    }
}
