package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.dto.FilterDto;
import bg.fmi.web.marketplace.exception.ResourceNotFoundException;
import bg.fmi.web.marketplace.model.product.Product;
import bg.fmi.web.marketplace.model.user.User;
import bg.fmi.web.marketplace.repository.ProductRepository;
import bg.fmi.web.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
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


}
