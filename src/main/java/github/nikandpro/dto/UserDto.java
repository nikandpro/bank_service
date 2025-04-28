package github.nikandpro.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;

}
