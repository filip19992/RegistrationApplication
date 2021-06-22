package pl.filipwlodarczyk.RegistrationApplication.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
