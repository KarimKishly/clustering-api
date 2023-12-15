package com.clustering.services;

import com.clustering.http.ApiResponse;
import com.clustering.http.KMeansRequest;
import com.clustering.kmeans.ClusterMean;
import com.clustering.kmeans.KMeansCore;
import com.clustering.utils.Cluster;
import com.clustering.utils.Utils;
import com.clustering.vectorization.Collection;
import com.clustering.vectorization.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class KMeansService {
    public ApiResponse cluster(KMeansRequest kMeansRequest) {
        int k = kMeansRequest.getK();
        Utils utils = new Utils(kMeansRequest);
        KMeansCore core = new KMeansCore();
        Collection collection = new Collection(kMeansRequest.isTiny());
        List<Cluster> allClusters = Stream.generate(() -> new Cluster(utils)).limit(k).toList();
        List<ClusterMean<Document, Cluster>> meansOfClusters = new ArrayList<>();
        List<Double> historyIntraClusterSimilarity = new ArrayList<>();
        List<Cluster> resultingCluster = new ArrayList<>();

        core.init(allClusters, collection, meansOfClusters);

        while (true) {
            core.findMostRelevantClusterPerDocument(allClusters, collection, meansOfClusters, utils);
            core.calculateNewCentroids(meansOfClusters);
            historyIntraClusterSimilarity.add(
                    allClusters.stream().mapToDouble(Cluster::getIntraClusterSimilarity).sum());
            if(historyIntraClusterSimilarity.size() > 1) {
                int size = historyIntraClusterSimilarity.size() - 1;
                if(historyIntraClusterSimilarity.get(size) < historyIntraClusterSimilarity.get(size-1)) {
                    break;
                }
            }
            resultingCluster = allClusters;
        }

        ApiResponse response = new ApiResponse();
        response.setClusters(resultingCluster);
        response.setDaviesBouldinIndex(utils.daviesBouldinIndex(resultingCluster));
        response.setDunnIndex(utils.dunnIndex(resultingCluster, utils));

        return response;
    }
}
