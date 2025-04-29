package github.nikandpro.controller;

import github.nikandpro.dto.*;
import github.nikandpro.dto.request.UpdateRequest;
import github.nikandpro.dto.request.UserCreateRequest;
import github.nikandpro.dto.request.UserSearchRequest;
import github.nikandpro.dto.response.UserResponseDto;
import github.nikandpro.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserDto createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/{userId}/emails")
    public ResponseEntity<EmailDataDto> addEmail(
            @PathVariable Long userId,
            @RequestBody UpdateRequest email) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUserEmail(userId, email.getEmail()));
    }

    @DeleteMapping("/{userId}/emails/{emailId}")
    public ResponseEntity<Void> removeEmail(
            @PathVariable Long userId,
            @PathVariable Long emailId) {

        userService.removeUserEmail(userId, emailId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/phones")
    public ResponseEntity<PhoneDataDto> addPhone(
            @PathVariable Long userId,
            @RequestBody UpdateRequest phone) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUserPhone(userId, phone.getPhone()));
    }

    @DeleteMapping("/{userId}/phones/{phoneId}")
    public ResponseEntity<Void> removePhone(
            @PathVariable Long userId,
            @PathVariable Long phoneId) {

        userService.removeUserPhone(userId, phoneId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/by-dob")
    public Page<UserResponseDto> findByDateOfBirth(
            @RequestBody UserSearchRequest request) {
        return userService.findByDateOfBirthAfter(request);
    }


    @GetMapping("/search/by-phone")
    public UserResponseDto findByPhone(
            @RequestBody UserSearchRequest request) {
        return userService.findByPhone(request);
    }


    @GetMapping("/search/by-name")
    public Page<UserResponseDto> findByNameStartingWith(
            @RequestBody UserSearchRequest request) {
        return userService.findByNameStartingWith(request);
    }


    @GetMapping("/search/by-email")
    public UserResponseDto findByEmail(
            @RequestBody UserSearchRequest request) {
        return userService.findByEmail(request);
    }
}
