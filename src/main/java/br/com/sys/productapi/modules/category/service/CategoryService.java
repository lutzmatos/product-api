package br.com.sys.productapi.modules.category.service;

import br.com.sys.productapi.config.exception.ValidationException;
import br.com.sys.productapi.config.response.SuccessResponse;
import br.com.sys.productapi.modules.category.dto.CategoryRequest;
import br.com.sys.productapi.modules.category.dto.CategoryResponse;
import br.com.sys.productapi.modules.category.model.Category;
import br.com.sys.productapi.modules.category.repository.CategoryRepository;
import br.com.sys.productapi.modules.product.repository.ProductRepository;
import br.com.sys.productapi.modules.supplier.dto.SupplierRequest;
import br.com.sys.productapi.modules.supplier.dto.SupplierResponse;
import br.com.sys.productapi.modules.supplier.model.Supplier;
import br.com.sys.productapi.modules.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public CategoryResponse findByIdResponse(Integer id) {
        return CategoryResponse.of(this.findById(id));
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository
                .findAll()
                .stream()
                .map(CategoryResponse::of)
                //.map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> findByDescription(String description) {

        if (isEmpty(description)) {
            throw new ValidationException("The category description must be informed");
        }

        return categoryRepository
                .findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(CategoryResponse::of)
                //.map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    }

    public Category findById(Integer id) {
        this.validateInformedId(id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException("There is no category for the given ID."));
    }

    public CategoryResponse save(CategoryRequest request) {
        this.validateCategoryNameInformed(request);
        var category = this.categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    public CategoryResponse updateById(CategoryRequest request, Integer id) {
        this.validateCategoryNameInformed(request);
        this.validateInformedId(id);
        var category = Category.of(request);
        category.setId(id);
        this.categoryRepository.save(category);
        return CategoryResponse.of(category);
    }

    private void validateCategoryNameInformed(CategoryRequest request) {
        if (isEmpty(request.getDescription()))
        {
            throw new ValidationException("The category description was not informed");
        }
    }

    public SuccessResponse deleteById (Integer id) {
        this.validateInformedId(id);
        if (this.productRepository.existsBySupplierId(id)) {
            throw new ValidationException("you cannot delete this category because it's already defined by a product");
        }
        this.categoryRepository.deleteById(id);
        return SuccessResponse.create("The category was deleted");
    }

    private void validateInformedId(Integer id) {
        if (isEmpty(id))
        {
            throw new ValidationException("The category ID was not informed");
        }
    }

}
