package br.com.sys.productapi.modules.product.model;

import br.com.sys.productapi.modules.category.model.Category;
import br.com.sys.productapi.modules.product.dto.ProductRequest;
import br.com.sys.productapi.modules.supplier.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "FK_SUPPLIER", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "FK_CATEGORY", nullable = false)
    private Category category;

    @Column(name = "QUANTITY_AVAILABLE", nullable = false)
    private Integer quantityAvailable;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public static Product of(ProductRequest request, Category category, Supplier supplier) {
        return Product
                .builder()
                .name(request.getName())
                .supplier(supplier)
                .category(category)
                .quantityAvailable(request.getQuantityAvailable())
                .createdAt(request.getCreatedAt())
                .build();
    }

    public void updateStock(Integer quantity) {
        this.quantityAvailable = this.quantityAvailable - (quantity);
    }

}
