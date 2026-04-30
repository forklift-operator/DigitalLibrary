package bg.fmi.web.marketplace.dto;

import bg.fmi.web.marketplace.model.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDto {
    @Size(max = 45)
    @NotBlank(message = "Name is required")
    @Email(message = "Not a valid email")
    private String email;

    @Size(max = 45, message = "Maximum characters is 45")
    @NotBlank(message = "First mame is required")
    private String firstName;

    @Size(max = 45, message = "Maximum characters is 45")
    @NotBlank(message = "Last mame is required")
    private String lastName;

    @Size(max = 45, message = "Maximum characters is 45")
    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

}
