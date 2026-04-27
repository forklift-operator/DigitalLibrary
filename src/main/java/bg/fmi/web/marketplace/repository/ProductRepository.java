package bg.fmi.web.marketplace.repository;

import bg.fmi.web.marketplace.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
