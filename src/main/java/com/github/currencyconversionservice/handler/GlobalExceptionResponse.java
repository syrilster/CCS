package com.github.currencyconversionservice.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GlobalExceptionResponse {
    private String code;
    private String description;
}
