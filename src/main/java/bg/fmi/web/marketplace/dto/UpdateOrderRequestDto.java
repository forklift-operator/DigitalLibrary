package bg.fmi.web.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequestDto {
    private Long orderId;
    private Long productId;
    private Integer quantity;
}
