package bg.fmi.web.marketplace.controller;

import bg.fmi.web.marketplace.dto.FilterDto;
import bg.fmi.web.marketplace.dto.ProductCreateDto;
import bg.fmi.web.marketplace.dto.ProductFullResponse;
import bg.fmi.web.marketplace.dto.ProductResponseDto;
import bg.fmi.web.marketplace.model.product.Product;
import bg.fmi.web.marketplace.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getProducts(@ModelAttribute FilterDto filter, HttpSession session) {
        List<Product> allProducts = productService.getAllProducts(filter);

        List<ProductResponseDto> response = allProducts.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductFullResponse> addProduct(@RequestBody ProductCreateDto createDto, HttpSession session) {

        Long userId = (Long) session.getAttribute("USER_ID");

        Product product = productService.saveProduct(modelMapper.map(createDto, Product.class), userId);

        ProductFullResponse response = modelMapper.map(product, ProductFullResponse.class);

        return ResponseEntity.status(200).body(response);
    }
}
