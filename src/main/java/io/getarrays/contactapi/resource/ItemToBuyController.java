package io.getarrays.contactapi.resource;
import io.getarrays.contactapi.dto.CartRespDto;
import io.getarrays.contactapi.service.ItemToBuyServices;
import io.getarrays.contactapi.domain.*;
import io.getarrays.contactapi.repo.ItemToBuyRepo;
import io.getarrays.contactapi.repo.OrderRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.getarrays.contactapi.repo.ProductRepo;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/cart")
public class ItemToBuyController {
    public final ProductRepo productRepo;
    public final ItemToBuyRepo itemToBuyRepo;
    public final OrderRepo orderRepo;
    public final ItemToBuyServices itemToBuyServices;

    public ItemToBuyController(ProductRepo productRepo, ItemToBuyRepo itemToBuyRepo, OrderRepo orderRepo, ItemToBuyServices itemToBuyServices) {
        this.productRepo = productRepo;
        this.itemToBuyRepo = itemToBuyRepo;
        this.orderRepo = orderRepo;
        this.itemToBuyServices = itemToBuyServices;
    }

    @GetMapping
    public Iterable<ItemToBuy> getAllProducts(){
        return itemToBuyRepo.findAll();
        //return allProds;
    }

    @PostMapping(path = "/{id}/{pName}")
    public ItemToBuy addToCart(@PathVariable String id,
                                   @PathVariable String pName,
                                   @RequestParam(defaultValue = "1") int quantity){
        return this.itemToBuyServices.addItemToCart(id, pName, quantity);
    }

    @GetMapping(path = "/{id}")
    public CartRespDto showAllItemInCart(@PathVariable String id){
        List<ItemToBuy> itemToBuyList = itemToBuyRepo.findByCustomerIdAndStatus(id, Status.pending);
        if(itemToBuyList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");
        }
        double sum = 0d;
        for (ItemToBuy item : itemToBuyList) {
            Optional<Product> product = productRepo.findById(item.getProductId());
            if (product.isPresent()) {
                sum += item.getQuantity() * product.get().getPrice();
            }
        }
        return new CartRespDto(itemToBuyList, sum);
    }

    @PutMapping(path = "/{id}")
    public ItemToBuy changeQuantityItems(@PathVariable String id, @RequestParam int quantity){
        Optional<ItemToBuy> itemToBuyOptional = this.itemToBuyRepo.findById(id);
        if(itemToBuyOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in cart");
        }
        ItemToBuy itemToBuy = itemToBuyOptional.get();

        itemToBuy.setQuantity(quantity);
        itemToBuyRepo.save(itemToBuy);
        return itemToBuy;

    }

    @DeleteMapping(path = "/{id}")
    public String deleteItemInCart(@PathVariable String id) {
        Optional<ItemToBuy> itemToBuyOptional = this.itemToBuyRepo.findById(id);
        if (itemToBuyOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in cart");
        }
        this.itemToBuyRepo.delete(itemToBuyOptional.get());
        return "Item deleted";
    }

    @PutMapping(path = "/{customerId}/checkout")
    public String checkOut(@PathVariable String customerId){
        return this.itemToBuyServices.checkOutCart(customerId);
    }
}
