package bg.fmi.web.marketplace;

import bg.fmi.web.marketplace.model.product.Product;
import bg.fmi.web.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArtisanMarketplace {

	public static void main(String[] args) {
		SpringApplication.run(ArtisanMarketplace.class, args);
	}

}
