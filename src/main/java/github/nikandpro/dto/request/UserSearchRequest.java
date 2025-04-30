package github.nikandpro.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchRequest {
    private String name;
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    private String email;
    @Size(min = 8, max = 20)
    private String phone;
    private Integer page = 0;
    private Integer size = 10;
}
