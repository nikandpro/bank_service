package github.nikandpro.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserCreateRequest {
    @Size(max = 500)
    @NotEmpty
    private String name;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Size(min = 8, max = 500)
    private String password;
}
