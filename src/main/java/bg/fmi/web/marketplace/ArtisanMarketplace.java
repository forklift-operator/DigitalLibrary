package bg.fmi.web.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class})
@EnableJdbcHttpSession
public class ArtisanMarketplace {

	public static void main(String[] args) {
		SpringApplication.run(ArtisanMarketplace.class, args);
	}

}
