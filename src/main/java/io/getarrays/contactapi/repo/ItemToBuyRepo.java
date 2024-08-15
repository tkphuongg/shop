package io.getarrays.contactapi.repo;

import io.getarrays.contactapi.domain.ItemToBuy;
import io.getarrays.contactapi.domain.Status;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemToBuyRepo extends JpaRepository<ItemToBuy, String> {
    Optional<ItemToBuy> findById(@Nullable String id);

    List<ItemToBuy> findByCustomerIdAndStatus(@Nullable String id, Status status);
}
