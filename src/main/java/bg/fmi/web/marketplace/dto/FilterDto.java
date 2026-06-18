package bg.fmi.web.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterDto {
    private String location;
    private Double minPrice;
    private Double maxPrice;
    private boolean availability;
    private List<String> keywords;
}
