package bg.fmi.web.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReqDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @PositiveOrZero
    private Double price;
    @PositiveOrZero
    private Integer quantity;
    @NotBlank
    private String location;
}
