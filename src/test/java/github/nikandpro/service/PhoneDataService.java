package github.nikandpro.service;

import github.nikandpro.dto.PhoneDataDto;
import github.nikandpro.entity.PhoneData;
import github.nikandpro.entity.User;
import github.nikandpro.exception.BadRequestException;
import github.nikandpro.exception.ConflictException;
import github.nikandpro.mapper.PhoneMapper;
import github.nikandpro.repository.PhoneDataRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhoneDataServiceTest {

    @Mock
    private PhoneDataRepository phoneDataRepository;
    @Mock
    private PhoneMapper phoneMapper;

    @InjectMocks
    private PhoneDataService phoneDataService;

    @Test
    void addPhoneToUser_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);
        String phone = "+123456789";
        PhoneData savedPhone = new PhoneData();
        PhoneDataDto expectedDto = new PhoneDataDto();

        when(phoneDataRepository.existsByPhone(phone)).thenReturn(false);
        when(phoneDataRepository.save(any())).thenReturn(savedPhone);
        when(phoneMapper.toDto(savedPhone)).thenReturn(expectedDto);

        // Act
        PhoneDataDto result = phoneDataService.addPhoneToUser(user, phone);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(phoneDataRepository).save(argThat(p ->
                p.getPhone().equals(phone) && p.getUser().equals(user)
        ));
    }

    @Test
    void addPhoneToUser_PhoneExists_ThrowsConflictException() {
        // Arrange
        String phone = "+123456789";
        when(phoneDataRepository.existsByPhone(phone)).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictException.class, () ->
                phoneDataService.addPhoneToUser(new User(), phone)
        );
    }

    @Test
    void addPhoneToUser_InvalidPhoneFormat_ThrowsBadRequest() {
        // Arrange
        String shortPhone = "123";

        // Act & Assert
        assertThrows(BadRequestException.class, () ->
                phoneDataService.addPhoneToUser(new User(), shortPhone)
        );
    }

    @Test
    void removePhoneFromUser_Success() {
        // Arrange
        Long userId = 1L;
        Long phoneId = 1L;

        User user = new User();
        user.setId(userId);

        PhoneData phone1 = new PhoneData();
        phone1.setId(phoneId);
        phone1.setUser(user);

        PhoneData phone2 = new PhoneData();
        phone2.setId(2L);
        phone2.setUser(user);

        user.setPhones(new HashSet<>(Arrays.asList(phone1, phone2)));

        when(phoneDataRepository.findById(phoneId)).thenReturn(Optional.of(phone1));

        // Act
        phoneDataService.removePhoneFromUser(userId, phoneId);

        // Assert
        verify(phoneDataRepository).delete(phone1);
    }

    @Test
    void removePhoneFromUser_PhoneNotFound_ThrowsException() {
        // Arrange
        when(phoneDataRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                phoneDataService.removePhoneFromUser(1L, 1L)
        );
    }

    @Test
    void validatePhoneOwnership_ValidOwner_NoException() {
        // Arrange
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        PhoneData phone1 = new PhoneData();
        phone1.setId(1L);
        phone1.setUser(user);

        PhoneData phone2 = new PhoneData();
        phone2.setId(2L);
        phone2.setUser(user);

        user.setPhones(new HashSet<>(Arrays.asList(phone1, phone2)));

        when(phoneDataRepository.findById(1L)).thenReturn(Optional.of(phone1));

        // Act & Assert
        assertDoesNotThrow(() ->
                phoneDataService.removePhoneFromUser(userId, 1L)
        );
    }

    @Test
    void validatePhoneOwnership_InvalidOwner_ThrowsException() {
        // Arrange
        Long ownerId = 1L;
        Long requesterId = 2L;

        User owner = new User();
        owner.setId(ownerId);

        PhoneData phoneData = new PhoneData();
        phoneData.setUser(owner);

        when(phoneDataRepository.findById(anyLong())).thenReturn(Optional.of(phoneData));

        // Act & Assert
        assertThrows(BadRequestException.class, () ->
                phoneDataService.removePhoneFromUser(requesterId, 1L)
        );
    }

    @Test
    void validateNotLastPhone_MultiplePhones_NoException() {
        // Arrange
        User user = new User();
        user.setId(1L);

        PhoneData phone1 = new PhoneData();
        phone1.setId(1L);
        phone1.setUser(user);

        PhoneData phone2 = new PhoneData();
        phone2.setId(2L);
        phone2.setUser(user);

        user.setPhones(new HashSet<>(Arrays.asList(phone1, phone2)));

        when(phoneDataRepository.findById(anyLong())).thenReturn(Optional.of(phone1));

        // Act & Assert
        assertDoesNotThrow(() ->
                phoneDataService.removePhoneFromUser(1L, 1L)
        );
    }

    @Test
    void validateNotLastPhone_LastPhone_ThrowsException() {
        // Arrange
        User user = new User();
        user.setId(1L);

        PhoneData phoneData = new PhoneData();
        phoneData.setUser(user);

        user.setPhones(new HashSet<>(Collections.singletonList(phoneData)));

        when(phoneDataRepository.findById(anyLong())).thenReturn(Optional.of(phoneData));

        // Act & Assert
        assertThrows(BadRequestException.class, () ->
                phoneDataService.removePhoneFromUser(1L, 1L)
        );
    }

    @Test
    void validatePhoneFormat_ValidPhone_NoException() {
        // Arrange
        String validPhone = "+123456789";

        // Act & Assert
        assertDoesNotThrow(() ->
                phoneDataService.addPhoneToUser(new User(), validPhone)
        );
    }

    @Test
    void validatePhoneFormat_NullPhone_ThrowsException() {
        // Act & Assert
        assertThrows(BadRequestException.class, () ->
                phoneDataService.addPhoneToUser(new User(), null)
        );
    }

    @Test
    void validatePhoneFormat_ShortPhone_ThrowsException() {
        // Arrange
        String shortPhone = "12345";

        // Act & Assert
        assertThrows(BadRequestException.class, () ->
                phoneDataService.addPhoneToUser(new User(), shortPhone)
        );
    }
}