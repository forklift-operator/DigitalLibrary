package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.model.product.Product;
import bg.fmi.web.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Long addProduct(Product product) {
        return productRepository.save(product).getId();
    }
}
