package bg.fmi.web.marketplace.controller;

import bg.fmi.web.marketplace.dto.ReviewAlterDto;
import bg.fmi.web.marketplace.dto.ReviewPostDto;
import bg.fmi.web.marketplace.dto.ReviewResponseDto;
import bg.fmi.web.marketplace.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/products/{product_id}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProductId(
        @PathVariable(name = "product_id") Long productId) {
        List<ReviewResponseDto> reviews = reviewService.getAllReviewsForProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/products/{product_id}/reviews")
    public ResponseEntity<ReviewResponseDto> addReview(@PathVariable(name = "product_id") Long productId,
                                                       @RequestBody @Validated ReviewPostDto dto) {
        ReviewResponseDto reviewResponseDto = reviewService.addReviewToProduct(productId, dto);
        return ResponseEntity.ok(reviewResponseDto);
    }

    @PatchMapping("/products/{product_id}/reviews/{review_id}")
    public ResponseEntity<ReviewResponseDto> alterReview(@PathVariable(name = "product_id") Integer productId,
                                                         @PathVariable(name = "review_id") Long reviewId,
                                                         @RequestBody ReviewAlterDto dto) {
        //TODO check if the user who tries to alter it is the owner of the review
        ReviewResponseDto reviewResponseDto = reviewService.alterReview(reviewId, dto);
        return ResponseEntity.ok(reviewResponseDto);
    }

    @PatchMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        //TODO check if the user who tries to delete it is the owner of the review
        reviewService.deleteReviewById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
