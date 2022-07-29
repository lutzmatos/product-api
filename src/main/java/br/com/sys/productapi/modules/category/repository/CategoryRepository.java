package br.com.sys.productapi.modules.category.repository;

import br.com.sys.productapi.modules.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByDescription(String description);
    List<Category> findByDescriptionIgnoreCaseContaining(String description);

}
