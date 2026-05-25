package bg.fmi.web.marketplace;

import bg.fmi.web.marketplace.model.product.Product;
import bg.fmi.web.marketplace.model.user.Role;
import bg.fmi.web.marketplace.model.user.User;
import bg.fmi.web.marketplace.repository.ProductRepository;
import bg.fmi.web.marketplace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class})
public class ArtisanMarketplace {

	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ArtisanMarketplace.class, args);

        UserService userService = context.getBean(UserService.class);

        User c1 = new User();
        c1.setFirstName("Stoyan");
        c1.setLastName("Yordanov");
        c1.setEmail("sy@g.c");
        c1.setPassword("BALLASD123132");
        c1.setRole(Role.ADMIN);

    }

}
