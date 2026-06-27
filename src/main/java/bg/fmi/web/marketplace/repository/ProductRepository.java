package bg.fmi.web.marketplace.repository;

import bg.fmi.web.marketplace.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByName(String name);

    List<Product> findByUserId(Long userId);

    <T> ScopedValue<T> name(String name);
}
