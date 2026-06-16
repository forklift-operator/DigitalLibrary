package bg.fmi.web.marketplace.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateRequestDto {
    @NotNull(message = "Product id is required")
    private Long productId;
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must not be negative")
    private Integer quantity;
}
