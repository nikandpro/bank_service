package github.nikandpro.service;

import github.nikandpro.dto.EmailDataDto;
import github.nikandpro.dto.UserDto;
import github.nikandpro.dto.request.RegistrationUserDto;
import github.nikandpro.dto.request.UserSearchRequest;
import github.nikandpro.dto.response.UserResponseDto;
import github.nikandpro.entity.EmailData;
import github.nikandpro.entity.User;
import github.nikandpro.exception.BadRequestException;
import github.nikandpro.mapper.UserMapper;
import github.nikandpro.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private EmailDataService emailDataService;
    @Mock
    private PhoneDataService phoneDataService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_Success() {
        // Arrange
        RegistrationUserDto registrationDto = new RegistrationUserDto();
        registrationDto.setEmails("test@mail.ru");
        registrationDto.setPassword("password");
        registrationDto.setName("Test User");

        User savedUser = new User();
        savedUser.setId(1L);

        when(userRepository.findById(any())).thenReturn(Optional.of(savedUser));
        when(userMapper.registrationDtoToUser(any(), any())).thenReturn(new User());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(savedUser);
        when(emailDataService.addEmailToUser(any(), anyString())).thenReturn(new EmailDataDto());

        // Act
        userService.createUser(registrationDto);

        // Assert
        verify(userRepository).save(any());
        verify(emailDataService).addEmailToUser(eq(savedUser), eq("test@mail.ru"));
        verify(accountService).createAccountForUser(any());
    }

    @Test
    void getUserById_UserExists_ReturnsDto() {
        // Arrange
        User user = new User();
        user.setId(1L);
        UserDto expectedDto = new UserDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(expectedDto);

        // Act
        UserDto result = userService.getUserById(1L);

        // Assert
        assertEquals(expectedDto, result);
    }

    @Test
    void getUserById_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void addUserEmail_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);
        EmailDataDto expectedDto = new EmailDataDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(emailDataService.addEmailToUser(any(), anyString())).thenReturn(expectedDto);

        // Act
        EmailDataDto result = userService.addUserEmail(1L, "test@mail.ru");

        // Assert
        assertEquals(expectedDto, result);
        verify(emailDataService).addEmailToUser(user, "test@mail.ru");
    }

    @Test
    void findByEmail_Success() {
        // Arrange
        User user = new User();
        UserResponseDto expectedDto = new UserResponseDto();

        when(userRepository.findByEmail("test@mail.ru")).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(expectedDto);

        // Act
        UserResponseDto result = userService.findByEmail("test@mail.ru");

        // Assert
        assertEquals(expectedDto, result);
    }

    @Test
    void loadUserByUsername_Success() {
        // Arrange
        User user = new User();
        user.setPassword("encodedPass");
        EmailData emailData = new EmailData();
        emailData.setEmail("test@mail.ru");
        user.setEmails(Set.of(emailData));

        when(userRepository.findByEmail("test@mail.ru")).thenReturn(user);

        // Act
        UserDetails userDetails = userService.loadUserByUsername("test@mail.ru");

        // Assert
        assertEquals("test@mail.ru", userDetails.getUsername());
        assertEquals("encodedPass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void loadUserByUsername_NoEmails_ThrowsException() {
        // Arrange
        User user = new User();
        user.setEmails(Collections.emptySet());

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> userService.loadUserByUsername("test@mail.ru"));
    }

    @Test
    void validateDateOfBirth_NullDate_ThrowsException() {
        assertThrows(BadRequestException.class,
                () -> userService.findByDateOfBirthAfter(
                        new UserSearchRequest("", null, "test@mail.ru","123",0,2)
                ));
    }

    @Test
    void findByDateOfBirthAfter_ValidDate_ReturnsPage() {
        // Arrange
        LocalDate testDate = LocalDate.of(1990, 1, 1);
        UserSearchRequest request = new UserSearchRequest(
                "Test", testDate, "test@mail.ru", "123456789", 0, 2
        );

        Page<User> mockPage = new PageImpl<>(List.of(new User()));
        when(userRepository.findByDateOfBirthAfter(eq(testDate), any(PageRequest.class)))
                .thenReturn(mockPage);

        UserResponseDto mockDto = new UserResponseDto();
        when(userMapper.toResponseDto(any())).thenReturn(mockDto);

        // Act & Assert
        assertDoesNotThrow(() -> {
            Page<UserResponseDto> result = userService.findByDateOfBirthAfter(request);
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
        });
    }

    @Test
    void findByNameStartingWith_Success() {
        // Arrange
        Page<User> page = new PageImpl<>(List.of(new User()));
        UserResponseDto dto = new UserResponseDto();

        when(userRepository.findByNameStartingWith(anyString(), any()))
                .thenReturn(page);
        when(userMapper.toResponseDto(any())).thenReturn(dto);

        // Act
        Page<UserResponseDto> result = userService.findByNameStartingWith(
                new UserSearchRequest("Test", LocalDate.of(1990,1,1), "test@mail.ru","123456789",0,2)
        );

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals(dto, result.getContent().get(0));
    }
}
