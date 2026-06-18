package bg.fmi.web.marketplace.dto;

import bg.fmi.web.marketplace.model.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    @NotNull(message = "Id is required")
    private Long id;

    @NotNull(message = "Id is required")
    @DecimalMin(value = "0.0")
    private Double totalAmount;

    @NotNull
    private Status status;

    @NotNull
    private List<OrderItemResponseDto> items;
}
