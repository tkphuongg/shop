package io.getarrays.contactapi.repo;

import io.getarrays.contactapi.domain.ItemToBuy;
import io.getarrays.contactapi.domain.Order;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, String> {
    Optional<Order> findByOrderId(@Nullable String id);

    List<Order> findByCustomerId(@Nullable String id);
}
