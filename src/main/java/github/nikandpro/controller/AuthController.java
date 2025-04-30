package github.nikandpro.controller;

import github.nikandpro.dto.request.JwtRequest;
import github.nikandpro.dto.request.RegistrationUserDto;
import github.nikandpro.dto.response.JwtResponse;
import github.nikandpro.service.UserService;
import github.nikandpro.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody JwtRequest authRequest) {
        log.info("auth request: {}", authRequest);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto) {
        userService.createUser(registrationUserDto);

        return ResponseEntity.ok("User registered");
    }
}
