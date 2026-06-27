package bg.fmi.web.marketplace.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto {
    private Long id;
    private String name;
    private String description;
    @PositiveOrZero
    private Double price;
    @PositiveOrZero
    private Integer quantity;
    private String location;
}
