package com.example.clusteredDataWarehouse.controller;

import com.example.clusteredDataWarehouse.dto.request.DealRequest;
import com.example.clusteredDataWarehouse.dto.response.ApiResponse;
import com.example.clusteredDataWarehouse.service.DealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fx-deals")
public class DealsController {

    private final DealService dealService;


    @PostMapping("/submit")
    public ResponseEntity<ApiResponse> submitDeal(@RequestBody @Valid DealRequest dealRequest){
        return dealService.submitRequest(dealRequest);
    }
}
