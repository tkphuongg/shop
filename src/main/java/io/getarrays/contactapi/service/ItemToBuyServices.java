package io.getarrays.contactapi.service;

import io.getarrays.contactapi.domain.ItemToBuy;
import io.getarrays.contactapi.domain.Order;
import io.getarrays.contactapi.domain.Product;
import io.getarrays.contactapi.domain.Status;
import io.getarrays.contactapi.repo.ItemToBuyRepo;
import io.getarrays.contactapi.repo.OrderRepo;
import io.getarrays.contactapi.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@jakarta.transaction.Transactional(rollbackOn = Exception.class)
//@RequiredArgsConstructor
public
class ItemToBuyServices {

    private final ItemToBuyRepo itemToBuyRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public ItemToBuyServices(ItemToBuyRepo itemToBuyRepo, ProductRepo productRepository, OrderRepo orderRepo) {
        this.itemToBuyRepo = itemToBuyRepo;
        this.productRepo = productRepository;
        this.orderRepo = orderRepo;
    }

    @Transactional
    public ItemToBuy addItemToCart(String id, String pName, int quantity){
        ItemToBuy newItem = new ItemToBuy(id, productRepo.findByName(pName).getId(), quantity, Status.pending);
        if(quantity > productRepo.findByName(pName).getQuantity()){
            throw new RuntimeException("Quantity exceeds inventory");
        }
        itemToBuyRepo.save(newItem);
        System.out.println("Item added to cart");
        return newItem;
    }

    @Transactional
    public String checkOutCart(String customerId){
        List<ItemToBuy> itemToBuys = this.itemToBuyRepo.findByCustomerIdAndStatus(customerId, Status.pending);
        if(itemToBuys.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");
        }
        List<String> itemToBuyIds = new ArrayList<>();
        for(ItemToBuy i : itemToBuys){
            itemToBuyIds.add(i.getId());
        }
        Order newOrder = new Order(customerId, itemToBuyIds, Status.pending);
        for(ItemToBuy i : itemToBuys){
            Optional<Product> product = productRepo.findById(i.getProductId());
            if(product.isPresent()){
                Product prod = product.get();
                int quantityCheck = prod.getQuantity() - i.getQuantity();
                if(quantityCheck < 0){
                    throw new RuntimeException("Quantity of " + prod.getName() + " exceeds inventory");
                }
                prod.setQuantity(quantityCheck);
                productRepo.save(prod);
                i.setStatus(Status.processed);
                this.itemToBuyRepo.save(i);
            }
        }
        this.orderRepo.save(newOrder);
        return "Order successfully placed";
    }



//    @Transactional
//    public Cart removeItemFromCart(Long cartId, Long productId) {
//        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
//        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
//        cart.getProducts().remove(product);
//
//        return cartRepository.save(cart);
//    }
//
//    public List<Product> getItemsFromCart(Long cartId) {
//        ItemToBuy cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
//        return cart.getProducts();
//    }
}
