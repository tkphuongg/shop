package io.getarrays.contactapi.repo;

import io.getarrays.contactapi.domain.Customer;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, String> {
        Optional<Customer> findById(@Nullable String id);
}
