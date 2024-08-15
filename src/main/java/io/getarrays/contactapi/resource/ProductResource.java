package io.getarrays.contactapi.resource;

import io.getarrays.contactapi.domain.Product;
import io.getarrays.contactapi.dto.ProductReqDto;
import io.getarrays.contactapi.repo.ProductRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/products")
//@RequiredArgsConstructor
public class ProductResource {
    //private final ProductService productService;

    private final ProductRepo productRepo;

    public ProductResource(ProductRepo productRepo){
        this.productRepo = productRepo;
    }

    @GetMapping
    public Iterable<Product> getAllProducts(){
        return productRepo.findAll();
        //return allProds;
    }

    @PostMapping(path = "/addNew")
    public Product addNewProduct(@RequestBody ProductReqDto request){
        Product newProduct = new Product(request.getName(), request.getPrice(), request.getQuantity());
        productRepo.save(newProduct);
        System.out.println("Product added");
        return newProduct;
    }


    @DeleteMapping(path = "/delete/{id}")
    public String deleteProduct(@PathVariable String id){
        Optional<Product> productOptional = this.productRepo.findById(id);
        if(productOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        this.productRepo.delete(productOptional.get());
        return "Product deleted";
    }

    @PutMapping(path = "/update/{id}")
    public Product updateProduct(@PathVariable String id,
                                 @RequestBody ProductReqDto updatedProduct){
        Optional<Product> productOptional = this.productRepo.findById(id);
        if(productOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        Product productToUpdate = productOptional.get();
        productToUpdate.setName(updatedProduct.getName());
        productToUpdate.setPrice(updatedProduct.getPrice());
        productToUpdate.setQuantity(updatedProduct.getQuantity());
        productRepo.save(productToUpdate);

        return productToUpdate;

    }

//    @PostMapping
//    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
//        //return ResponseEntity.ok().body(productService.createContact(product));
//        return ResponseEntity.created(URI.create("/product/userID")).body(productService.createProduct(product));
//    }

//    @GetMapping
//    public ResponseEntity<Page<Product>> getProduct(@RequestParam(value = "page", defaultValue = "0") int page,
//                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
//        return ResponseEntity.ok().body(productService.getAllProducts(page, size));
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Product> getProduct(@PathVariable(value = "id") String id) {
//        return ResponseEntity.ok().body(productService.getProduct(id));
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id") String id) {
//        productService.deleteProduct(id);
//        return ResponseEntity.noContent().build();
//    }



//    @RequestMapping(value = "/{id}", method = DELETE)
//    @ResponseBody
//    public String deleteFile(@PathVariable("id") String id) {
//        productService.deleteProduct(id);
//        return "File deleted ";
//    }

}