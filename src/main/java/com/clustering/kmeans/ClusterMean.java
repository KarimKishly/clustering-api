package com.clustering.kmeans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClusterMean<A, B> {
    private final A document;
    private final B cluster;

}
