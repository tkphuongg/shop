package io.getarrays.contactapi.service;

import io.getarrays.contactapi.domain.ItemToBuy;
import io.getarrays.contactapi.domain.Order;
import io.getarrays.contactapi.domain.Status;
import io.getarrays.contactapi.repo.ItemToBuyRepo;
import io.getarrays.contactapi.repo.OrderRepo;
import io.getarrays.contactapi.repo.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class OrderService {
    private final ItemToBuyRepo itemToBuyRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public OrderService(ItemToBuyRepo itemToBuyRepo, ProductRepo productRepo, OrderRepo orderRepo) {
        this.itemToBuyRepo = itemToBuyRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    public String cancelOrder(String orderId) {
        Optional<Order> myOrderOptional = this.orderRepo.findById(orderId);
        if (myOrderOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        Order myOrder = myOrderOptional.get();
        if (myOrder.getStatus() == Status.processed) {
            throw new RuntimeException("Cannot cancel order");
        }

        List<String> itemsToOrder = myOrder.getOrderedItems();
        for (String s : itemsToOrder) {
            ItemToBuy itemInCart = this.itemToBuyRepo.findById(s).get();
            int quantItem = itemInCart.getQuantity();
            int quantInventory = this.productRepo.findById(itemInCart.getProductId()).get().getQuantity();
            this.productRepo.findById(itemInCart.getProductId()).get().setQuantity(quantInventory + quantItem);
            this.itemToBuyRepo.delete(itemInCart);
        }
        this.orderRepo.delete(myOrder);
        return "Order cancelled";
    }
}
