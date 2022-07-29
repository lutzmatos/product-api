package br.com.sys.productapi.modules.supplier.service;

import br.com.sys.productapi.config.exception.ValidationException;
import br.com.sys.productapi.config.response.SuccessResponse;
import br.com.sys.productapi.modules.product.repository.ProductRepository;
import br.com.sys.productapi.modules.supplier.dto.SupplierRequest;
import br.com.sys.productapi.modules.supplier.dto.SupplierResponse;
import br.com.sys.productapi.modules.supplier.model.Supplier;
import br.com.sys.productapi.modules.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    public SupplierResponse findByIdResponse(Integer id) {
        return SupplierResponse.of(this.findById(id));
    }

    public List<SupplierResponse> findAll() {
        return supplierRepository
                .findAll()
                .stream()
                .map(SupplierResponse::of)
                //.map(category -> SupplierResponse.of(category))
                .collect(Collectors.toList());
    }

    public List<SupplierResponse> findByName(String name) {

        if (isEmpty(name)) {
            throw new ValidationException("The supplier name must be informed");
        }

        return supplierRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());
    }

    public Supplier findById(Integer id) {
        this.validateInformedId(id);
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ValidationException("There is no supplier for the given ID."));
    }

    public SupplierResponse save(SupplierRequest request) {
        this.validateNameInformed(request);
        var supplier = this.supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    public SupplierResponse updateById(SupplierRequest request, Integer id) {
        this.validateNameInformed(request);
        this.validateInformedId(id);
        var supplier = Supplier.of(request);
        supplier.setId(id);
        this.supplierRepository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    private void validateNameInformed(SupplierRequest request) {
        if (isEmpty(request.getName()))
        {
            throw new ValidationException("The supplier name was not informed");
        }
    }

    public SuccessResponse deleteById (Integer id) {
        this.validateInformedId(id);
        if (this.productRepository.existsBySupplierId(id)) {
            throw new ValidationException("you cannot delete this supplier because it's already defined by a product");
        }
        this.supplierRepository.deleteById(id);
        return SuccessResponse.create("The supplier was deleted");
    }

    private void validateInformedId(Integer id) {
        if (isEmpty(id))
        {
            throw new ValidationException("The supplier ID was not informed");
        }
    }
}
