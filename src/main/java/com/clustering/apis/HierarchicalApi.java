package com.clustering.apis;

import com.clustering.http.HierarchicalRequest;
import com.clustering.http.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface HierarchicalApi {
    public ResponseEntity<ApiResponse> hierarchicalClustering(HierarchicalRequest hierarchicalRequest);
}
