package github.nikandpro.service;

import github.nikandpro.dto.PhoneDataDto;
import github.nikandpro.entity.PhoneData;
import github.nikandpro.entity.User;
import github.nikandpro.exception.BadRequestException;
import github.nikandpro.exception.ConflictException;
import github.nikandpro.mapper.PhoneMapper;
import github.nikandpro.repository.PhoneDataRepository;
import github.nikandpro.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhoneDataService {
    private final PhoneDataRepository phoneDataRepository;
    private final PhoneMapper phoneMapper;
    private static final int MIN_PHONE_LENGTH = 8;

    public PhoneDataDto addPhoneToUser(User user, String phone) {
        validatePhoneFormat(phone);

        if (phoneDataRepository.existsByPhone(phone)) {
            throw new ConflictException("Phone already in use");
        }

        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(phone);
        phoneData.setUser(user);

        return phoneMapper.toDto(phoneDataRepository.save(phoneData));
    }

    public void removePhoneFromUser(Long userId, Long phoneId) {
        PhoneData phoneData = phoneDataRepository.findById(phoneId)
                .orElseThrow(() -> new EntityNotFoundException("Phone not found"));

        validatePhoneOwnership(userId, phoneData);
        validateNotLastPhone(phoneData);

        phoneDataRepository.delete(phoneData);
    }

    private void validatePhoneFormat(String phone) {
        if (phone == null || phone.length() < MIN_PHONE_LENGTH) {
            throw new BadRequestException("Phone number must be at least " + MIN_PHONE_LENGTH + " characters");
        }
    }

    private void validatePhoneOwnership(Long userId, PhoneData phoneData) {
        if (!phoneData.getUser().getId().equals(userId)) {
            throw new BadRequestException("Phone does not belong to user");
        }
    }

    private void validateNotLastPhone(PhoneData phoneData) {
        if (phoneData.getUser().getPhones().size() <= 1) {
            throw new BadRequestException("Cannot delete the last phone");
        }
    }
}
