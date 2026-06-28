package bg.fmi.web.marketplace.repository;

import bg.fmi.web.marketplace.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByProductId(Long productId);
}
