package com.clustering.controllers;

import com.clustering.apis.HierarchicalApi;
import com.clustering.http.HierarchicalRequest;
import com.clustering.http.ApiResponse;
import com.clustering.services.HierarchicalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class HierarchicalController implements HierarchicalApi {
    HierarchicalService hierarchicalService;

    @Override
    @CrossOrigin
    @PostMapping("/hierarchical")
    public ResponseEntity<ApiResponse> hierarchicalClustering(
            @RequestBody HierarchicalRequest hierarchicalRequest) {
        System.out.println(hierarchicalRequest);
        ApiResponse response = hierarchicalService.cluster(hierarchicalRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
