package com.clustering.services;

import com.clustering.hierarchical.ClusterSimilarity;
import com.clustering.hierarchical.HierarchicalCore;
import com.clustering.http.HierarchicalRequest;
import com.clustering.http.ApiResponse;
import com.clustering.utils.Cluster;
import com.clustering.utils.Utils;
import com.clustering.vectorization.Collection;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HierarchicalService {
    public ApiResponse cluster(HierarchicalRequest hierarchicalRequest) {
        HierarchicalCore core = new HierarchicalCore();
        Utils utils = new Utils(hierarchicalRequest);

        Map<String, ClusterSimilarity<Cluster, Cluster, Double>> cache = new HashMap<>();
        Collection collection = new Collection(hierarchicalRequest.isTiny());

        List<Cluster> allClusters = core.getAllClusters(collection, utils);
        List<Double> similarityRatio = new ArrayList<>();
        List<Cluster> resultingCluster = new ArrayList<>();
        List<List<List<Integer>>> dendrogram = new ArrayList<>();
        int numberOfDecreases = 0;
        Utils.populateDendrogram(dendrogram, allClusters);
        while (true) {
            ClusterSimilarity<Cluster, Cluster, Double> mostSimilarClusters =
                    core.getMostSimilarClusters(allClusters, cache, utils);
            allClusters = core.combineClusters(mostSimilarClusters, allClusters, cache, utils);
            core.addClusterSimilarityRatioToList(allClusters, similarityRatio, utils);
            Utils.populateDendrogram(dendrogram, allClusters);

            if (!core.similarityRatioIncreasing(similarityRatio)) {
                if (numberOfDecreases == 0) {
                    resultingCluster = allClusters;
                }
                numberOfDecreases += 1;
            } else {
                numberOfDecreases = 0;
            }
            if (numberOfDecreases == hierarchicalRequest.getDecreases()) {
                break;
            }
        }

        ApiResponse response = new ApiResponse();
        response.setClusters(resultingCluster);
        response.setDaviesBouldinIndex(utils.daviesBouldinIndex(resultingCluster));
        response.setDunnIndex(utils.dunnIndex(resultingCluster, utils));
        //Utils.writeResults(resultingCluster);
        return response;
    }
}
