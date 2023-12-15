package com.clustering.apis;

import com.clustering.http.ApiResponse;
import com.clustering.http.KMeansRequest;
import org.springframework.http.ResponseEntity;

public interface KMeansApi {
    public ResponseEntity<ApiResponse> kMeansClustering(KMeansRequest kMeansRequest);
}
