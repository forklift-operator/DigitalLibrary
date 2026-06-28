package bg.fmi.web.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class VendorStatsDto {
    private long vendorId;
    private double avgRating;
    private int totalSales;
    private int totalProducts;
}
