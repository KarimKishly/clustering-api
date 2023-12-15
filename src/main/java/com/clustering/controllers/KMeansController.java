package com.clustering.controllers;

import com.clustering.apis.KMeansApi;
import com.clustering.http.ApiResponse;
import com.clustering.http.KMeansRequest;
import com.clustering.services.KMeansService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class KMeansController implements KMeansApi {
    KMeansService kMeansService;
    @Override
    @PostMapping("/kmeans")
    public ResponseEntity<ApiResponse> kMeansClustering(@RequestBody KMeansRequest kMeansRequest) {
        ApiResponse response = kMeansService.cluster(kMeansRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
