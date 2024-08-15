package io.getarrays.contactapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class ProductReqDto {
    private String name;
    private double price;
    private int quantity;
}
