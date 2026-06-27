package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.dto.ReviewAlterDto;
import bg.fmi.web.marketplace.dto.ReviewPostDto;
import bg.fmi.web.marketplace.dto.ReviewResponseDto;
import bg.fmi.web.marketplace.exception.ResourceNotFoundException;
import bg.fmi.web.marketplace.model.Product;
import bg.fmi.web.marketplace.model.Review;
import bg.fmi.web.marketplace.repository.ProductRepository;
import bg.fmi.web.marketplace.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, ModelMapper mapper) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    public List<ReviewResponseDto> getAllReviewsForProduct(Long productId) {
        return reviewRepository.findByProductId(productId)
            .stream()
            .map(reviewEntity -> mapper.map(reviewEntity, ReviewResponseDto.class))
            .toList();
    }

    public ReviewResponseDto addReviewToProduct(Long productId, ReviewPostDto dto) {
        Optional<Product> productToAddReview = productRepository.findById(productId);
        if (productToAddReview.isEmpty()) {
            throw new ResourceNotFoundException("Product", productId);
        }

        Review review = mapper.map(dto, Review.class);
        review.setId(null);
        review.setProduct(productToAddReview.get());
        reviewRepository.save(review);
        return mapper.map(review, ReviewResponseDto.class);
    }

    public ReviewResponseDto alterReview(Long reviewId, ReviewAlterDto dto) {
        if (dto.getText() == null && dto.getStars() == null) {
            throw new IllegalArgumentException("Nothing to alter");
        }

        Optional<Review> optionalReviewToAlter = reviewRepository.findById(reviewId);
        if (optionalReviewToAlter.isEmpty()) {
            throw new ResourceNotFoundException("Review", reviewId);
        }
        Review reviewToAlter = optionalReviewToAlter.get();
        if (dto.getText() != null) {
            reviewToAlter.setText(dto.getText());
        }
        if (dto.getStars() != null) {
            reviewToAlter.setStars(dto.getStars());
        }

        Review alteredReview = reviewRepository.save(reviewToAlter);
        return mapper.map(alteredReview, ReviewResponseDto.class);
    }

    public void deleteReviewById(Long id) {
        reviewRepository.deleteById(id);
    }
}
