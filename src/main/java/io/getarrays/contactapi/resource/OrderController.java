package io.getarrays.contactapi.resource;

import io.getarrays.contactapi.domain.Order;
import io.getarrays.contactapi.domain.Status;
import io.getarrays.contactapi.repo.ItemToBuyRepo;
import io.getarrays.contactapi.repo.OrderRepo;
import io.getarrays.contactapi.repo.ProductRepo;
import io.getarrays.contactapi.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    public final ProductRepo productRepo;
    public final ItemToBuyRepo itemToBuyRepo;
    public final OrderRepo orderRepo;
    public final OrderService orderService;

    public OrderController(ProductRepo productRepo, ItemToBuyRepo itemToBuyRepo, OrderRepo orderRepo, OrderService orderService) {
        this.productRepo = productRepo;
        this.itemToBuyRepo = itemToBuyRepo;
        this.orderRepo = orderRepo;
        this.orderService = orderService;
    }

    @GetMapping
    public Iterable<Order> viewAllOrders(){
        return this.orderRepo.findAll();
    }

    @GetMapping(path = "/{customerId}")
    public Iterable<Order> viewMyOrder(@PathVariable String customerId){
        List<Order> customerOrderList =  this.orderRepo.findByCustomerId(customerId);
        if(customerOrderList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return customerOrderList;
    }

    @GetMapping(path = "/{customerId}/{orderId}")
    public Order viewOrder(@PathVariable String customerId, @PathVariable String orderId){
        Optional<Order> myOrderOptional =  this.orderRepo.findById(orderId);
        if(myOrderOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return myOrderOptional.get();
    }

    @DeleteMapping(path = "/{customerId}/{orderId}")
    public String cancelOrders(@PathVariable String customerId, @PathVariable String orderId){
        return this.orderService.cancelOrder(orderId);
    }

    @DeleteMapping(path = "/{orderId}")
    public String deleteOrder(@PathVariable String orderId){
        Optional<Order> orderToDeleteOptional = this.orderRepo.findByOrderId(orderId);
        if(orderToDeleteOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        Order orderToDelete = orderToDeleteOptional.get();
        this.orderRepo.delete(orderToDelete);
        return "Order deleted";
    }

    @PutMapping(path = "/{orderId}")
    public Order processOrder(@PathVariable String orderId){
        Optional<Order> orderOptional = this.orderRepo.findByOrderId(orderId);
        if(orderOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        Order order = orderOptional.get();
        order.setStatus(Status.processed);
        orderRepo.save(order);
        return order;
    }


}
