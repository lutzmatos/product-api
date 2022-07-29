package br.com.sys.productapi.modules.category.controller;

import br.com.sys.productapi.config.response.SuccessResponse;
import br.com.sys.productapi.modules.category.dto.CategoryRequest;
import br.com.sys.productapi.modules.category.dto.CategoryResponse;
import br.com.sys.productapi.modules.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest request) {
        return this.categoryService.save(request);
    }

    @GetMapping
    public List<CategoryResponse> findAll() {
        return this.categoryService.findAll();
    }

    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable(name = "id") Integer id) {
        return this.categoryService.findByIdResponse(id);
    }

    @GetMapping("description/{description}")
    public List<CategoryResponse> findByDescription(@PathVariable(name = "description") String description) {
        return this.categoryService.findByDescription(description);
    }

    @DeleteMapping("{id}")
    public SuccessResponse deleteById(@PathVariable Integer id) {
        return this.categoryService.deleteById(id);
    }

    @PutMapping("{id}")
    public CategoryResponse updateById(@RequestBody CategoryRequest request, @PathVariable Integer id) {
        return this.categoryService.updateById(request, id);
    }

}
