package br.com.sys.productapi.modules.product.service;

import br.com.sys.productapi.config.exception.ValidationException;
import br.com.sys.productapi.config.response.SuccessResponse;
import br.com.sys.productapi.modules.category.service.CategoryService;
import br.com.sys.productapi.modules.product.dto.*;
import br.com.sys.productapi.modules.product.model.Product;
import br.com.sys.productapi.modules.product.repository.ProductRepository;
import br.com.sys.productapi.modules.sales.client.SalesClient;
import br.com.sys.productapi.modules.sales.dto.SalesConfirmationDTO;
import br.com.sys.productapi.modules.sales.dto.SalesProductResponse;
import br.com.sys.productapi.modules.sales.enums.SalesStatus;
import br.com.sys.productapi.modules.sales.rabbitmq.SalesConfirmationSender;
import br.com.sys.productapi.modules.supplier.service.SupplierService;
import br.com.sys.productapi.utils.RequestUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class ProductService {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION_ID = "transactionid";
    private static final String SERVICE_ID = "serviceid";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SalesConfirmationSender salesConfirmationSender;

    @Autowired
    private SalesClient salesClient;

    public Product findById(Integer id) {

        if (isEmpty(id)) {
            throw new ValidationException("The product identification must be informed");
        }

        return productRepository.findById(id)
                .orElseThrow(() -> new ValidationException("There is no product for the given ID."));
    }

    public ProductResponse findByIdResponse(Integer id) {
        return ProductResponse.of(this.findById(id));
    }

    public List<ProductResponse> findAll() {
        return this.productRepository
                .findAll()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByName(String name) {

        if (isEmpty(name)) {
            throw new ValidationException("The category description must be informed");
        }

        return this.productRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId) {

        if (isEmpty(categoryId)) {
            throw new ValidationException("The category ID must be informed");
        }

        return this.productRepository
                .findByCategoryId(categoryId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId) {

        if (isEmpty(supplierId)) {
            throw new ValidationException("The supplier ID must be informed");
        }

        return this.productRepository
                .findBySupplierId(supplierId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse save(ProductRequest request) {
        this.validate(request);
        var category = this.categoryService.findById(request.getCategoryId());
        var supplier = this.supplierService.findById(request.getSupplierId());
        var product = this.productRepository.save(Product.of(request, category, supplier));
        return ProductResponse.of(product);
    }

    public ProductResponse updateById(ProductRequest request, Integer id) {
        this.validate(request);
        this.validateInformedId(id);
        var category = this.categoryService.findById(request.getCategoryId());
        var supplier = this.supplierService.findById(request.getSupplierId());
        var product = Product.of(request, category, supplier);
        product.setId(id);
        this.productRepository.save(product);
        return ProductResponse.of(product);
    }

    public Boolean existsByCategoryId(Integer categoryId)
    {
        return this.productRepository.existsByCategoryId(categoryId);
    }

    public Boolean existsBySupplierId(Integer supplierId)
    {
        return this.productRepository.existsBySupplierId(supplierId);
    }

    public SuccessResponse deleteById (Integer id) {
        this.validateInformedId(id);
        this.productRepository.deleteById(id);
        return SuccessResponse.create("The product was deleted");
    }

    public SuccessResponse checkProductsStock (ProductCheckStockRequest request) {


        try {

            var currentRequest = RequestUtil.getCurrentRequest();
            var transactionId =  currentRequest.getAttribute(TRANSACTION_ID);
            var serviceId =  currentRequest.getAttribute(SERVICE_ID);

            log.info(
                    "Request [{}] data [{}] transactionId [{}] serviceId [{}]",
                    currentRequest.getMethod(),
                    new ObjectMapper().writeValueAsString(currentRequest),
                    transactionId,
                    serviceId
            );

            this.validateProductCheckStockRequest(request);

            request
                    .getProducts()
                    .forEach(this::validateProductCheckStock);

            var response = SuccessResponse.create("The stock is ok");
            log.info(
                    "Response [{}] data [{}] transactionId [{}] serviceId [{}]",
                    currentRequest.getMethod(),
                    new ObjectMapper().writeValueAsString(response),
                    transactionId,
                    serviceId
            );

            return response;

        } catch (Exception exception) {
            throw new ValidationException(exception.getMessage());
        }

    }

    public ProductSalesResponse findProductSales(Integer id) {
        try {

            var request = RequestUtil.getCurrentRequest();

            String transactionId =  (String) request.getHeader("transactionid");
            String serviceId =  (String) request.getHeader("serviceid");
            //String authorization =   (String) request.getHeader("Authorization");

//            log.info("transactionId {}", transactionId);
//            log.info("serviceId {}", serviceId);
//            log.info("authorization {}", authorization);
//
//            log.info(
//                    "Request [{}] data [{}] transactionId [{}] serviceId [{}]",
//                    currentRequest.getMethod(),
//                    new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).writeValueAsString(currentRequest),
//                    transactionId,
//                    serviceId
//            );

            log.info("1");
            var product = this.findById(id);
            log.info("2 productId: {}", product.getId());
            var sales = this.getSalesByProductId(product.getId());
            log.info("3");
            var response = ProductSalesResponse.of(product, sales.getResult().getSalesIds());

            log.info(
                    "Response [{}] transactionId [{}] serviceId [{}]",
                    request.getMethod(),
                    transactionId,
                    serviceId
            );

            return response;

        } catch (Exception exception) {
            log.info(exception.getMessage());
//            exception.printStackTrace();
            throw new ValidationException("There was an error trying to get the product's sales");
        }
    }

    private SalesProductResponse getSalesByProductId(Integer productId) {
        try {
            return  this.salesClient
                    .findSalesByProductId(productId)
                    .orElseThrow(
                        () -> {
                            throw new ValidationException("The sales was not found by this product");
                        }
                    );
        } catch (Exception exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    @Transactional
    public void updateProductStock(ProductStockDTO productStockDTO) {

        // Lista de produtos para atualizar
        List<Product> productsToUpdate = new ArrayList<Product>();

        try {

            // Validação
            this.validateStockUpdateData(productStockDTO);

            // Atualização no banco de dados
            productStockDTO
                .getProducts()
                .forEach(
                    salesProduct -> {
                        Product existingProduct = this.findById(salesProduct.getProductId());
                        this.validateQuantityInStock(salesProduct, existingProduct);
                        existingProduct.updateStock(salesProduct.getQuantity());
                        productsToUpdate.add(existingProduct);
                    }
                );

            // Se chegou aqui já podemos atualizar os produtos
            //productsToUpdate.forEach(
            //    product -> {
            //        this.productRepository.save(product);
            //    }
            //);
            if (!isEmpty(productsToUpdate)) this.productRepository.saveAll(productsToUpdate);

            // Resposta para o "listener"
            var approvedMessage = new SalesConfirmationDTO(productStockDTO.getSalesId(), SalesStatus.APPROVED, productStockDTO.getTransactionid());
            this.salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);

        } catch (Exception exception) {

            log.error("updateProductStock Exception: {}", exception.getMessage());

            var rejectedMessage = new SalesConfirmationDTO(productStockDTO.getSalesId(), SalesStatus.REJECTED, productStockDTO.getTransactionid());
            this.salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);

        }
    }

    /**
     * @brief Validações em geral.
     * @ignore
     */

    private void validate(ProductRequest request) {

        // Validação do nome
        if (isEmpty(request.getName()))
        {
            throw new ValidationException("The product name was not informed");
        }

        // Validação da quantidade
        if (request.getCategoryId() < 0)
        {
            throw new ValidationException("The product quantity was not informed");
        }

        // Validação do fornecedor
        if (isEmpty(request.getSupplierId()))
        {
            throw new ValidationException("The product supplier was not informed");
        }

        // Validação da categoria
        if (isEmpty(request.getCategoryId()))
        {
            throw new ValidationException("The product category was not informed");
        }

    }

    private void validateInformedId(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The product ID was not informed");
        }
    }

    private void validateStockUpdateData(ProductStockDTO productStockDTO) {
        if (isEmpty(productStockDTO)) {
            throw new ValidationException("The sales is invalid");
        }
        if (isEmpty(productStockDTO.getSalesId())) {
            throw new ValidationException("The sales ID was not informed");
        }
        if (isEmpty(productStockDTO.getProducts())) {
            throw new ValidationException("The products list was not informed");
        }
        productStockDTO.getProducts().forEach(
            salesProduct -> {
                if (isEmpty(salesProduct.getQuantity())
                || isEmpty(salesProduct.getProductId())) {
                    throw new ValidationException("The product on the list is invalid");
                }
            }
        );
    }

    private void validateQuantityInStock(ProductStockQuantityDTO productStockQuantityDTO, Product product) {
        if (productStockQuantityDTO.getQuantity() > product.getQuantityAvailable()) {
            throw new ValidationException(
                    String.format("The product %s is out of stock.", product.getId())
            );
        }
    }

    private void validateProductCheckStockRequest(ProductCheckStockRequest request) {
        if (isEmpty(request)) {
            throw new ValidationException("The product request was not informed");
        }
        if (isEmpty(request.getProducts())) {
            throw new ValidationException("The products of request was not informed");
        }
    }

    private void validateProductCheckStock(ProductStockQuantityDTO productStockQuantityDTO) {

        if (isEmpty(productStockQuantityDTO)) {
            throw new ValidationException("The product stock quantity is invalid");
        }

        if (isEmpty(productStockQuantityDTO.getProductId())) {
            throw new ValidationException("The product stock ID must be informed");
        }

        if (isEmpty(productStockQuantityDTO.getQuantity())) {
            throw new ValidationException("The product stock quantity must be informed");
        }

        var product = this.findById(productStockQuantityDTO.getProductId());

        if (productStockQuantityDTO.getQuantity() > product.getQuantityAvailable()) {
            throw new ValidationException(
                String.format("The product %s is out of stock", product.getId())
            );
        }

    }

}
