package com.example.clusteredDataWarehouse.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class ErrorResponse {

    private String code;
    private List<String> messages;

    public static ErrorResponse with (String code, String message){
        return ErrorResponse.builder()
                .code(code)
                .messages(Collections.singletonList(message))
                .build();
    }
}
