package bg.fmi.web.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
public class ReviewAlterDto {
    @NotBlank
    @Size(max = 250)
    private String text;
    @Range(min = 0, max = 5)
    private Integer stars;
}
