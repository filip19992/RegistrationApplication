package pl.filipwlodarczyk.RegistrationApplication.product;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table
public class Product {

    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(generator = "product_sequence", strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long id;
    @Column(name = "product_name")
    private String name;
    @Column(name = "type_of_product")
    private Type type;

    public Product(String name, Type type) {
        this.name = name;
        this.type = type;
    }

}
