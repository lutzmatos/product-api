package br.com.sys.productapi.modules.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductRequest {
    private String name;
    @JsonProperty("supplier_id")
    private Integer supplierId;
    @JsonProperty("category_id")
    private Integer categoryId;
    @JsonProperty("quantity_available")
    private Integer quantityAvailable;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
