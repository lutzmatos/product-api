package br.com.sys.productapi.modules.product.controller;

import br.com.sys.productapi.config.response.SuccessResponse;
import br.com.sys.productapi.modules.category.dto.CategoryRequest;
import br.com.sys.productapi.modules.product.dto.ProductCheckStockRequest;
import br.com.sys.productapi.modules.product.dto.ProductRequest;
import br.com.sys.productapi.modules.product.dto.ProductResponse;
import br.com.sys.productapi.modules.product.dto.ProductSalesResponse;
import br.com.sys.productapi.modules.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest request) {
        return productService.save(request);
    }

    @GetMapping
    public List<ProductResponse> findAll() {
        return this.productService.findAll();
    }

    @GetMapping("{id}")
    public ProductResponse findById(@PathVariable(name = "id") Integer id) {
        return this.productService.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<ProductResponse> findByName(@PathVariable(name = "name") String name) {
        return this.productService.findByName(name);
    }

    @GetMapping("category/{categoryId}")
    public List<ProductResponse> findByCategoryId(@PathVariable(name = "categoryId") Integer categoryId) {
        return this.productService.findByCategoryId(categoryId);
    }

    @GetMapping("supplier/{supplierId}")
    public List<ProductResponse> findBySupplierId(@PathVariable(name = "supplierId") Integer supplierId) {
        return this.productService.findBySupplierId(supplierId);
    }

    @DeleteMapping("{id}")
    public SuccessResponse deleteById(@PathVariable Integer id) {
        return this.productService.deleteById(id);
    }

    @PutMapping("{id}")
    public ProductResponse updateById(@RequestBody ProductRequest request, @PathVariable Integer id) {
        return this.productService.updateById(request, id);
    }

    @PostMapping("check-stock")
    public SuccessResponse checkProductsStock(@RequestBody ProductCheckStockRequest request) {
        return this.productService.checkProductsStock(request);
    }

    @GetMapping("{id}/sales")
    public ProductSalesResponse findProductSales(@PathVariable Integer id) {
        return this.productService.findProductSales(id);
    }

}
