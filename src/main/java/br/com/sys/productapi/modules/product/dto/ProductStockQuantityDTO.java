package br.com.sys.productapi.modules.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockQuantityDTO {
    private Integer productId;
    private Integer quantity;
}
