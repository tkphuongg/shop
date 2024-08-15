package io.getarrays.contactapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class CustomerReqDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

}