package io.getarrays.contactapi.resource;

import io.getarrays.contactapi.domain.Customer;
import io.getarrays.contactapi.domain.Product;
import io.getarrays.contactapi.dto.CustomerReqDto;
import io.getarrays.contactapi.dto.ProductReqDto;
import io.getarrays.contactapi.repo.CustomerRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerResource {
    //public final ProductRepo productRepo;
    public final CustomerRepo customerRepo;

    public CustomerResource(CustomerRepo customerRepo){
        this.customerRepo = customerRepo;
    }

    @GetMapping
    public Iterable<Customer> getAllProducts(){
        return customerRepo.findAll();
        //return allProds;
    }

    @PostMapping(path = "/addNew")
    public Customer addNewProduct(@RequestBody CustomerReqDto request){
        Customer newCustomer = new Customer(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhoneNumber());
        customerRepo.save(newCustomer);
        System.out.println("Customer added");
        return newCustomer;
    }


    @DeleteMapping(path = "/delete/{id}")
    public String deleteCustomer(@PathVariable String id){
        Optional<Customer> customerOptional = this.customerRepo.findById(id);
        if(customerOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        this.customerRepo.delete(customerOptional.get());
        return "Customer deleted";
    }

    @PutMapping(path = "/update/{id}")
    public Customer updateCustomer(@PathVariable String id,
                                 @RequestBody CustomerReqDto updatedCustomer){
        Optional<Customer> customerOptional = this.customerRepo.findById(id);
        if(customerOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        Customer customerToUpdate = customerOptional.get();
        customerToUpdate.setFirstName(updatedCustomer.getFirstName());
        customerToUpdate.setLastName(updatedCustomer.getLastName());
        customerToUpdate.setPhoneNumber(updatedCustomer.getPhoneNumber());
        customerToUpdate.setEmail(updatedCustomer.getEmail());
        customerRepo.save(customerToUpdate);

        return customerToUpdate;
    }

//    @PostMapping(path = "/{id}/{pName}")
//    public ItemToBuy addItemToCart(@PathVariable String id,
//                                   @PathVariable String pName,
//                                   @RequestParam(defaultValue = "1") int quantity){
//        ItemToBuy newItem = new ItemToBuy(String id, String pName, quantity);
//
//    }


}
