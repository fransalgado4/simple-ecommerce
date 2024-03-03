package com.francisco.simple.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetail {
    private String title;
    private int status;
    private String detail;
    private LocalDateTime timeStamp;
}
