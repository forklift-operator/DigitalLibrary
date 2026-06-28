package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.dto.VendorStatsDto;
import bg.fmi.web.marketplace.model.Product;
import bg.fmi.web.marketplace.model.Review;
import bg.fmi.web.marketplace.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {
    private final ProductService productService;
    private final UserService userService;

    public VendorService(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    public VendorStatsDto getVendorStats(Long vendorId) {

        boolean isVendor = userService.getUserById(vendorId).getRole().equals(Role.VENDOR);
        if (!isVendor) {
            throw new IllegalArgumentException("User is not a vendor");
        }

        List<Product> allProductsByVendorId = productService.getAllProductsByVendorId(vendorId);

        double averageRating =
            allProductsByVendorId.stream().mapToDouble(p -> p.getReviews().stream().mapToDouble(Review::getStars).sum())
                .average().orElse(0.0);

        int totalQuantity = allProductsByVendorId.stream().mapToInt(Product::getQuantity).sum();

        return VendorStatsDto.builder()
            .setVendorId(vendorId)
            .setAvgRating(averageRating)
            .setTotalProducts(totalQuantity)
            .build();
    }
}
