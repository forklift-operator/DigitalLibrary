package bg.fmi.web.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFullResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String location;
}
