package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.dto.FilterDto;
import bg.fmi.web.marketplace.dto.ProductFullResponse;
import bg.fmi.web.marketplace.dto.ProductUpdateDto;
import bg.fmi.web.marketplace.exception.ResourceNotFoundException;
import bg.fmi.web.marketplace.exception.UnauthorisedException;
import bg.fmi.web.marketplace.model.Product;
import bg.fmi.web.marketplace.model.User;
import bg.fmi.web.marketplace.repository.ProductRepository;
import bg.fmi.web.marketplace.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public ProductService(ProductRepository productRepository, UserRepository userRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }


    public Product saveProduct(Product product, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), userId));

        product.setUser(user);

        return productRepository.save(product);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), id));
    }

    public Product getProduct(String name) {
        // make the exception better
        return productRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), ""));
    }

    public List<Product> getAllProductsByVendorId(Long vendorId) {
        if (!userRepository.existsById(vendorId)) {
            throw new ResourceNotFoundException(User.class.getSimpleName(), vendorId);
        }
        return productRepository.findByUserId(vendorId);
    }

    public List<Product> getAllProducts(FilterDto filter) {
        Specification<Product> spec = Specification.where(
                (root, query, cb) -> cb.conjunction()
        );

        if (filter.getLocation() != null && !filter.getLocation().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("location"), "%" + filter.getLocation() + "%"));
        }

        if (filter.getMaxPrice() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
        }

        if (filter.getMinPrice() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
        }

        if (filter.getKeywords() != null) {
            for (String keyword : filter.getKeywords()) {
                if (!keyword.isBlank()) {
                    spec = spec.and((root, query, cb) ->
                            cb.like(root.get("description"), "%" + keyword + "%"));
                }
            }
        }

        return productRepository.findAll(spec);
    }

    public void updateQuantity(long id, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be less than zero");
        }
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), id));
        product.setQuantity(quantity);
        productRepository.save(product);
    }

    public ProductFullResponse updateProduct(long id, ProductUpdateDto productReqDto) {
        if (id < 0) {
            throw new IllegalArgumentException("Id cannot be less than zero");
        }
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), id));
        String newName = productReqDto.getName();

        String newDescription = productReqDto.getDescription();
        Double newPrice = productReqDto.getPrice();
        Integer newQuantity = productReqDto.getQuantity();
        String newLocation = productReqDto.getLocation();

        product.setName(newName != null ? newName : product.getName());
        product.setDescription(newDescription != null ? newDescription : product.getDescription());
        product.setPrice(newPrice != null ? newPrice : product.getPrice());
        product.setQuantity(newQuantity != null ? newQuantity : product.getQuantity());
        product.setLocation(newLocation != null ? newLocation : product.getLocation());
        productRepository.save(product);

        return mapper.map(product, ProductFullResponse.class);
    }

    public void deleteProduct(long productId, long userId) {
        if (productId < 0) {
            throw new IllegalArgumentException("Id cannot be less than zero");
        }
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), productId));
        if (product.getUser().getId() != userId) {
            throw new UnauthorisedException();
        }
        productRepository.deleteById(productId);
    }
}
