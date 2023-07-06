package com.mobicoolsoft.electronic.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.net.http.HttpResponse;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class ApiResponseMessage {

    private String message;

    private Boolean success;

    private HttpStatus status;
}
