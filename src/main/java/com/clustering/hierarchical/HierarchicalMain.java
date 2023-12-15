package com.clustering.hierarchical;

import com.clustering.utils.Cluster;
import com.clustering.utils.Utils;
import com.clustering.vectorization.Collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//public class HierarchicalMain {
//    public static void main(String[] args) throws IOException {
//        HierarchicalCore core = new HierarchicalCore();
//
//        Map<String, ClusterSimilarity<Cluster, Cluster, Double>> cache = new HashMap<>();
//        Collection collection = new Collection();
//
//        List<Cluster> allClusters = core.getAllClusters(collection);
//        List<Double> similarityRatio = new ArrayList<>();
//        List<Cluster> resultingCluster = new ArrayList<>();
//        List<List<List<Integer>>> dendrogram = new ArrayList<>();
//        int numberOfDecreases = 0;
//        Utils.populateDendrogram(dendrogram, allClusters);
//        while (true) {
//            ClusterSimilarity<Cluster, Cluster, Double> mostSimilarClusters = core.getMostSimilarClusters(allClusters, cache);
//            allClusters = core.combineClusters(mostSimilarClusters, allClusters, cache);
//            core.addClusterSimilarityRatioToList(allClusters, similarityRatio);
//            Utils.populateDendrogram(dendrogram, allClusters);
//
//            if (!core.similarityRatioIncreasing(similarityRatio)) {
//                if (numberOfDecreases == 0) {
//                    resultingCluster = allClusters;
//                }
//                numberOfDecreases += 1;
//            } else {
//                numberOfDecreases = 0;
//            }
//            if (numberOfDecreases == 4) {
//                break;
//            }
//        }
//        System.out.println("Davies-Bouldin Index: " + Utils.daviesBouldinIndex(resultingCluster));
//        System.out.println("Dunn Index: " + Utils.dunnIndex(resultingCluster));
//        System.out.println(dendrogram);
//        Utils.writeResults(resultingCluster);
//    }
//}