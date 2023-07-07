package com.mobicoolsoft.electronic.store.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.net.http.HttpResponse;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class ApiResponseMessage {

    private String message;

    private Boolean success;

    private HttpStatus status;

}
