package bg.fmi.web.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
public class ReviewPostDto {
    @NotBlank
    @Size(max = 250)
    private String text;
    @Range(min = 0, max = 5)
    private Integer starts;
    @NotNull
    @PositiveOrZero
    private Integer userId;
}
