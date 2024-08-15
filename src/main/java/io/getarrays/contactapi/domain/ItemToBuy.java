package io.getarrays.contactapi.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import io.getarrays.contactapi.repo.ProductRepo;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableTransactionManagement
@JsonInclude(NON_DEFAULT)
@Table(name = "itemtobuy")
public class ItemToBuy {
    @Id
    @UuidGenerator
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    String productId;
    String customerId;
    int quantity;
    Status status = Status.pending;
    //double price;
//    @ManyToOne
//    @JoinColumn(name = "productId")
//    private Product product;
//
//    @ManyToOne
//    @JoinColumn(name = "customerId")
//    private Customer customer;

    public ItemToBuy(String customerId, String productId, int quantity, Status status){
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
        //this.price = 0;
    }
}
