package io.getarrays.contactapi.dto;

import io.getarrays.contactapi.domain.ItemToBuy;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartRespDto {
    List<ItemToBuy> items;
    Double total;
}
