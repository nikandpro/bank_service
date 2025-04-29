package github.nikandpro.dto.request;

import jakarta.validation.constraints.NotEmpty;
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
public class UserCreateRequest {
    @Size(max = 500)
    @NotEmpty
    private String name;

    @Past(message = "Date of birth must be in the past")
    @NotNull
    private LocalDate dateOfBirth;

    @Size(min = 8, max = 500)
    private String password;

    public @Size(max = 500) @NotEmpty String getName() {
        return name;
    }

    public void setName(@Size(max = 500) @NotEmpty String name) {
        this.name = name;
    }

    public @Past(message = "Date of birth must be in the past") @NotNull LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@Past(message = "Date of birth must be in the past") @NotNull LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public @Size(min = 8, max = 500) String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 8, max = 500) String password) {
        this.password = password;
    }
}
