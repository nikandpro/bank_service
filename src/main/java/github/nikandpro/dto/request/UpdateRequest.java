package github.nikandpro.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateRequest {
    private String email;
    @Size(min = 8, max = 20)
    private String phone;
}
