package br.com.sys.productapi.modules.supplier.controller;

import br.com.sys.productapi.config.response.SuccessResponse;
import br.com.sys.productapi.modules.supplier.dto.SupplierRequest;
import br.com.sys.productapi.modules.supplier.dto.SupplierResponse;
import br.com.sys.productapi.modules.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public SupplierResponse save(@RequestBody SupplierRequest request) {
        return this.supplierService.save(request);
    }

    @GetMapping
    public List<SupplierResponse> findAll() {
        return this.supplierService.findAll();
    }

    @GetMapping("{id}")
    public SupplierResponse findById(@PathVariable(name = "id") Integer id) {
        return this.supplierService.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<SupplierResponse> findByDescription(@PathVariable(name = "name") String name) {
        return this.supplierService.findByName(name);
    }

    @DeleteMapping("{id}")
    public SuccessResponse deleteById(@PathVariable Integer id) {
        return this.supplierService.deleteById(id);
    }

    @PutMapping("{id}")
    public SupplierResponse updateById(@RequestBody SupplierRequest request, @PathVariable Integer id) {
        return this.supplierService.updateById(request, id);
    }

}
