package com.clustering.hierarchical;

import com.clustering.utils.Cluster;
import com.clustering.utils.Utils;
import com.clustering.vectorization.Document;
import com.clustering.vectorization.Collection;

import java.util.*;

public class HierarchicalCore {
    public List<Cluster> getAllClusters(Collection collection, Utils utils) {
        List<Cluster> allClusters = new ArrayList<>();
        for (Document document : collection.getDocuments()) {
            allClusters.add(new Cluster(document, utils));
        }
        return allClusters;
    }

    public ClusterSimilarity<Cluster, Cluster, Double> getMostSimilarClusters(
            List<Cluster> allClusters,
            Map<String, ClusterSimilarity<Cluster, Cluster, Double>> cache, Utils utils) {
        PriorityQueue<ClusterSimilarity<Cluster, Cluster, Double>> pq = new PriorityQueue<>(new ClusterComparator<>());
        for (int i = 0; i < allClusters.size(); i++) {
            for (int j = i + 1; j < allClusters.size() - 1; j++) {
                ClusterSimilarity<Cluster, Cluster, Double> currentSimilarity = checkCache(
                        cache, allClusters.get(i), allClusters.get(j));
                if (currentSimilarity == null) {
                    currentSimilarity = new ClusterSimilarity<>(
                            allClusters.get(i),
                            allClusters.get(j),
                            utils.interClusterSimilarity(allClusters.get(i), allClusters.get(j))
                    );
                    cache.put(allClusters.get(i).hashCode() + String.valueOf(allClusters.get(j).hashCode()),
                            currentSimilarity
                    );
                }
                pq.add(currentSimilarity);
            }
        }
        return pq.remove();
    }

    public List<Cluster> combineClusters(
            ClusterSimilarity<Cluster, Cluster, Double> topCluster,
            List<Cluster> allClusters,
            Map<String, ClusterSimilarity<Cluster, Cluster, Double>> cache,
            Utils utils
    ) {
        System.out.println();
        allClusters.remove(topCluster.getFirstCluster());
        allClusters.remove(topCluster.getSecondCluster());
        cache.remove(topCluster.getFirstCluster().hashCode() + String.valueOf(topCluster.getSecondCluster().hashCode()));
        allClusters.add(new Cluster(topCluster.getFirstCluster(), topCluster.getSecondCluster(), utils));
        return allClusters;
    }

    public void addClusterSimilarityRatioToList(
            List<Cluster> allClusters,
            List<Double> ratio,
            Utils utils
    ) {
        double intraClusterSimilarity = getSumIntraClusterSimilarity(allClusters);
        double interClusterSimilarity = getInterClusterSimilarity(allClusters, utils);
        ratio.add(intraClusterSimilarity / interClusterSimilarity);
        System.out.println(intraClusterSimilarity);
        System.out.println(interClusterSimilarity);
        System.out.println(ratio);
    }

    public boolean similarityRatioIncreasing(List<Double> ratio) {
        if (ratio.size() > 1) {
            return ratio.get(ratio.size() - 1) > ratio.get(ratio.size() - 2);
        }
        return true;
    }

    private ClusterSimilarity<Cluster, Cluster, Double> checkCache(
            Map<String, ClusterSimilarity<Cluster, Cluster, Double>> cache, Cluster c1, Cluster c2) {
        String id = c1.hashCode() + String.valueOf(c2.hashCode());

        ClusterSimilarity<Cluster, Cluster, Double> calculatedCluster = cache.getOrDefault(id, null);

        if (calculatedCluster == null) {
            id = c2.hashCode() + String.valueOf(c1.hashCode());
            calculatedCluster = cache.getOrDefault(id, null);
        }

        return calculatedCluster;
    }

    private double getSumIntraClusterSimilarity(List<Cluster> allClusters) {
        double sumIntraClusterSimilarity = 0.0;
        for (Cluster cluster : allClusters) {
            sumIntraClusterSimilarity += cluster.getIntraClusterSimilarity()/allClusters.size();
        }
        return sumIntraClusterSimilarity;
    }

    private double getInterClusterSimilarity(List<Cluster> allClusters, Utils utils) {
        double intraClusterSimilarity = 0.0;
        for (int i = 0; i < allClusters.size(); i++) {
            double documentIntraClusterSimilarity = 0.0;
            for (int j = i + 1; j < allClusters.size() - 1; j++) {
                documentIntraClusterSimilarity += (utils.interClusterSimilarity(allClusters.get(i), allClusters.get(j))/
                        (allClusters.size()*(allClusters.size()-1)));
            }
            intraClusterSimilarity += documentIntraClusterSimilarity;
        }
        return intraClusterSimilarity;
    }
}
