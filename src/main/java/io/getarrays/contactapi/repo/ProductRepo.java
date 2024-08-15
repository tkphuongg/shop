package io.getarrays.contactapi.repo;

import io.getarrays.contactapi.domain.Product;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
    Optional<Product> findById(@Nullable String id);
    Product findByName(@Nullable String name);
    List<Product> findByIdIn(List<String> idList);
}