package github.nikandpro.service;

import github.nikandpro.dto.EmailDataDto;
import github.nikandpro.dto.PhoneDataDto;
import github.nikandpro.dto.request.UserCreateRequest;
import github.nikandpro.dto.UserDto;
import github.nikandpro.entity.EmailData;
import github.nikandpro.entity.PhoneData;
import github.nikandpro.entity.User;
import github.nikandpro.exception.BadRequestException;
import github.nikandpro.exception.ConflictException;
import github.nikandpro.mapper.EmailMapper;
import github.nikandpro.mapper.PhoneMapper;
import github.nikandpro.mapper.UserMapper;
import github.nikandpro.repository.EmailDataRepository;
import github.nikandpro.repository.PhoneDataRepository;
import github.nikandpro.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailMapper emailMapper;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final PhoneMapper phoneMapper;


    public UserDto createUser(UserCreateRequest request) {
        User savedUser = userRepository.save(userMapper.toUser(request));

        return userMapper.toUserDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow();
    }


    public EmailDataDto addUserEmail(Long userId, String email) {
        log.info(email);
        if (emailDataRepository.existsByEmail(email)) {
            throw new ConflictException("Email already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        EmailData emailData = new EmailData();
        emailData.setEmail(email);
        emailData.setUser(user);
        log.info(emailData.toString());

        return emailMapper.toDto(emailDataRepository.save(emailData));
    }


    public void removeUserEmail(Long userId, Long emailId) {
        EmailData emailData = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new EntityNotFoundException("Email not found"));

        if (!emailData.getUser().getId().equals(userId)) {
            throw new BadRequestException("Email does not belong to user");
        }

        if (emailData.getUser().getEmails().size() <= 1) {
            throw new BadRequestException("Cannot delete the last email");
        }

        emailDataRepository.delete(emailData);
    }


    public PhoneDataDto addUserPhone(Long userId, String phone) {
        if (phoneDataRepository.existsByPhone(phone)) {
            throw new ConflictException("Phone already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(phone);
        phoneData.setUser(user);


        return phoneMapper.toDto(phoneDataRepository.save(phoneData));
    }


    public void removeUserPhone(Long userId, Long phoneId) {
        PhoneData phoneData = phoneDataRepository.findById(phoneId)
                .orElseThrow(() -> new EntityNotFoundException("Phone not found"));

        if (!phoneData.getUser().getId().equals(userId)) {
            throw new BadRequestException("Phone does not belong to user");
        }

        if (phoneData.getUser().getPhones().size() <= 1) {
            throw new BadRequestException("Cannot delete the last phone");
        }

        phoneDataRepository.delete(phoneData);
    }
}
