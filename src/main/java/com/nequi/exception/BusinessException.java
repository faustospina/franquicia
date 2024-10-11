package com.nequi.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusinessException extends Exception {

    private String message;
    public BusinessException(String message) {
        this.message = message;
    }


}
