//package com.clustering.kmeans;
//
//import com.clustering.utils.Cluster;
//import com.clustering.utils.Utils;
//import com.clustering.vectorization.Collection;
//import com.clustering.vectorization.Document;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Stream;
//
//public class KMeansMain {
//    public static void main(String[] args) {
//        int k = 20;
//        KMeansCore core = new KMeansCore();
//        Collection collection = new Collection();
//        List<Cluster> allClusters = Stream.generate(Cluster::new).limit(k).toList();
//        List<ClusterMean<Document, Cluster>> meansOfClusters = new ArrayList<>();
//        List<Double> historyIntraClusterSimilarity = new ArrayList<>();
//        List<Cluster> resultingCluster = new ArrayList<>();
//
//        core.init(allClusters, collection, meansOfClusters);
//
//        while (true) {
//            core.findMostRelevantClusterPerDocument(allClusters, collection, meansOfClusters);
//            core.calculateNewCentroids(meansOfClusters);
//            historyIntraClusterSimilarity.add(
//                    allClusters.stream().mapToDouble(Cluster::getIntraClusterSimilarity).sum());
//            if(historyIntraClusterSimilarity.size() > 1) {
//                int size = historyIntraClusterSimilarity.size() - 1;
//                if(historyIntraClusterSimilarity.get(size) < historyIntraClusterSimilarity.get(size-1)) {
//                    break;
//                }
//            }
//            resultingCluster = allClusters;
//        }
//        try {
//            System.out.println("Davies-Bouldin Index: " + Utils.daviesBouldinIndex(resultingCluster));
//            System.out.println("Dunn Index: " + Utils.dunnIndex(resultingCluster));
//            Utils.writeKResults(resultingCluster);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
