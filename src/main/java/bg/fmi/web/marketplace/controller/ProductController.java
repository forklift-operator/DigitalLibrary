package bg.fmi.web.marketplace.controller;

import bg.fmi.web.marketplace.dto.FilterDto;
import bg.fmi.web.marketplace.dto.ProductFullResponse;
import bg.fmi.web.marketplace.dto.ProductReqDto;
import bg.fmi.web.marketplace.dto.ProductResponseDto;
import bg.fmi.web.marketplace.dto.ProductUpdateDto;
import bg.fmi.web.marketplace.model.Product;
import bg.fmi.web.marketplace.service.AuthService;
import bg.fmi.web.marketplace.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductService productService;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, AuthService authService, ModelMapper modelMapper) {
        this.productService = productService;
        this.authService = authService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getProducts(@ModelAttribute FilterDto filter, HttpSession session) {
        List<Product> allProducts = productService.getAllProducts(filter);

        List<ProductResponseDto> response = allProducts.stream()
            .map(product -> modelMapper.map(product, ProductResponseDto.class))
            .toList();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/products/vendor/{vendor_id}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByVendorId(@PathVariable Long vendor_id) {
        List<Product> allProducts = productService.getAllProductsByVendorId(vendor_id);

        List<ProductResponseDto> response = allProducts.stream()
            .map(product -> modelMapper.map(product, ProductResponseDto.class))
            .toList();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductFullResponse> getProduct(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        ProductFullResponse response = modelMapper.map(product, ProductFullResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductFullResponse> addProduct(@RequestBody ProductReqDto createDto,
                                                          HttpSession session) {

        Long userId = (Long) session.getAttribute("USER_ID");

        Product product = productService.saveProduct(modelMapper.map(createDto, Product.class), userId);

        ProductFullResponse response = modelMapper.map(product, ProductFullResponse.class);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductFullResponse> updateProduct(@PathVariable Long id,
                                                             @RequestBody ProductUpdateDto productReqDto) {
        ProductFullResponse productFullResponse = productService.updateProduct(id, productReqDto);

        return ResponseEntity.ok(productFullResponse);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId, HttpSession session) {
        Long userId = (Long) session.getAttribute("USER_ID");
        productService.deleteProduct(productId, userId);
        return ResponseEntity.noContent().build();
    }
}
