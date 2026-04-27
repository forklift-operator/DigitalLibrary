package bg.fmi.web.marketplace.model.product;

import bg.fmi.web.marketplace.model.review.Review;
import bg.fmi.web.marketplace.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private String location;
    private String description;
    @OneToMany(mappedBy = "product")
    private List<Review> reviews;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
