package github.nikandpro.controller;

import github.nikandpro.dto.request.TransferRequest;
import github.nikandpro.service.TransferService;
import github.nikandpro.service.UserService;
import github.nikandpro.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;

    @PostMapping("transfers")
    public ResponseEntity<?> transferMoney(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody TransferRequest request) {

        String token = authHeader.substring(7);
        String senderEmail = jwtTokenUtils.getEmailFromToken(token);

        Long senderUserId = userService.findByEmail(senderEmail).getId();

        transferService.transferMoney(senderUserId, request);
        return ResponseEntity.ok("Transfer completed successfully");
    }
}
