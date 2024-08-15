package io.getarrays.contactapi.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.awt.*;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Table(name = "order")
public class Order {
    @Id
    @UuidGenerator
    @Column(name = "orderId", unique = true, updatable = false)
    private String orderId;
    private String customerId;
    private List<String> orderedItems;
    private Status status;

    public Order(String customerId, List<String> orderedItems, Status status){
        this.customerId = customerId;
        this.orderedItems = orderedItems;
        this.status = status;
    }
}
