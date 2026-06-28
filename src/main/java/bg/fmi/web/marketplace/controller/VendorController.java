package bg.fmi.web.marketplace.controller;

import bg.fmi.web.marketplace.dto.VendorStatsDto;
import bg.fmi.web.marketplace.service.VendorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/vendors")

public class VendorController {
    private VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/{vendorId}/statistics")
    public VendorStatsDto getStatistics(@PathVariable Long vendorId) {
        return vendorService.getVendorStats(vendorId);
    }
}
