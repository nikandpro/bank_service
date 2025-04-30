package github.nikandpro.service;

import github.nikandpro.dto.EmailDataDto;
import github.nikandpro.dto.PhoneDataDto;
import github.nikandpro.dto.UserDto;
import github.nikandpro.dto.request.RegistrationUserDto;
import github.nikandpro.dto.request.UserSearchRequest;
import github.nikandpro.dto.response.UserResponseDto;
import github.nikandpro.entity.Account;
import github.nikandpro.entity.User;
import github.nikandpro.exception.BadRequestException;
import github.nikandpro.mapper.UserMapper;
import github.nikandpro.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailDataService emailDataService;
    private final PhoneDataService phoneDataService;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;


    public void createUser(RegistrationUserDto registrationDto) {
        User user = new User();
        user = userMapper.registrationDtoToUser(registrationDto, user);
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        User savedUser = userRepository.save(user);
        addUserEmail(savedUser.getId(), registrationDto.getEmails());
        accountService.createAccountForUser(savedUser);

        userMapper.toUserDto(savedUser);
    }


    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    public EmailDataDto addUserEmail(Long userId, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return emailDataService.addEmailToUser(user, email);
    }

    public void removeUserEmail(Long userId, Long emailId) {
        emailDataService.removeEmailFromUser(userId, emailId);
    }

    public PhoneDataDto addUserPhone(Long userId, String phone) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        return phoneDataService.addPhoneToUser(user, phone);
    }

    public void removeUserPhone(Long userId, Long phoneId) {
        phoneDataService.removePhoneFromUser(userId, phoneId);
    }

    public Page<UserResponseDto> findByDateOfBirthAfter(UserSearchRequest request) {
        validateDateOfBirth(request.getDateOfBirth());

        return userRepository.findByDateOfBirthAfter(
                request.getDateOfBirth(),
                PageRequest.of(request.getPage(), request.getSize())
        ).map(userMapper::toResponseDto);
    }

    public UserResponseDto findByPhone(UserSearchRequest request) {
        return userMapper.toResponseDto(
                userRepository.findByPhone(request.getPhone())
        );
    }

    public UserResponseDto findByEmail(UserSearchRequest request) {
        return userMapper.toResponseDto(
                userRepository.findByEmail(request.getEmail())
        );
    }

    public Page<UserResponseDto> findByNameStartingWith(UserSearchRequest request) {
        return userRepository.findByNameStartingWith(
                request.getName(),
                PageRequest.of(request.getPage(), request.getSize())
        ).map(userMapper::toResponseDto);
    }

    private void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new BadRequestException("Date of birth is empty");
        }
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        String userEmail = user.getEmails().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User has no emails"))
                .getEmail();

        return org.springframework.security.core.userdetails.User.builder()
                .username(userEmail)
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
