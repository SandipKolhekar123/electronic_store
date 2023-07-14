package com.mobicoolsoft.electronic.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class ImageResponse {

    private String imageName;

    private String message;

    private Boolean success;

    private HttpStatus status;
}
