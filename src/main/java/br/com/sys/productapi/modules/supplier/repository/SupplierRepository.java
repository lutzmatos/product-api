package br.com.sys.productapi.modules.supplier.repository;

import br.com.sys.productapi.modules.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    List<Supplier> findByNameIgnoreCaseContaining(String description);
    List<Supplier> findByName(String description);
}
