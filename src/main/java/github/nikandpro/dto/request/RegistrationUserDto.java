package github.nikandpro.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrationUserDto {
    @NotNull
    private String emails;
    private String name;
    @NotNull
    private String password;
    private LocalDate dateOfBirth;
}
