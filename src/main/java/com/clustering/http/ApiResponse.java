package com.clustering.http;
import com.clustering.utils.Cluster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
    List<Cluster> clusters;
    double daviesBouldinIndex;
    double dunnIndex;
}
