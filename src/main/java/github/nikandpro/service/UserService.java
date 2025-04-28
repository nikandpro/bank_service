package github.nikandpro.service;

import github.nikandpro.dto.UserCreateRequest;
import github.nikandpro.dto.UserDto;
import github.nikandpro.entity.User;
import github.nikandpro.mapper.UserMapper;
import github.nikandpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserDto createUser(UserCreateRequest request) {
        User user = userMapper.toUser(request);
        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        return userMapper.toUserDto(user.orElseThrow());
    }
}
