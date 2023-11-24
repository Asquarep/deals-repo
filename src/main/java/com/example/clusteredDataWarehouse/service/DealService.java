package com.example.clusteredDataWarehouse.service;

import com.example.clusteredDataWarehouse.dto.request.DealRequest;
import com.example.clusteredDataWarehouse.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface DealService {
    ResponseEntity<ApiResponse> submitRequest(DealRequest dealRequest);
}
