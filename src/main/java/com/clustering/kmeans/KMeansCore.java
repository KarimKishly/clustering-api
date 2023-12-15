package com.clustering.kmeans;

import com.clustering.utils.Cluster;
import com.clustering.utils.Utils;
import com.clustering.vectorization.Collection;
import com.clustering.vectorization.Document;

import java.util.ArrayList;
import java.util.List;

public class KMeansCore {
    public void init(
            List<Cluster> allClusters, Collection collection,
            List<ClusterMean<Document, Cluster>> meansOfClusters
    ) {
        for (Cluster cluster : allClusters) {;
            Document initialMean = collection.getDocuments().get((int) (Math.random() * collection.size()));
            ClusterMean<Document, Cluster> clusterMean = new ClusterMean<>(initialMean, cluster);
            meansOfClusters.add(clusterMean);
            cluster.getDocuments().add(initialMean);
        }
    }

    public void findMostRelevantClusterPerDocument(
            List<Cluster> allClusters, Collection collection,
            List<ClusterMean<Document, Cluster>> meansOfClusters,
            Utils utils
    ) {
        List<Document> clonedDocuments = new ArrayList<>(collection.getDocuments());
        for (Document document : clonedDocuments) {
            boolean isCentroid = false;
            for(ClusterMean<Document, Cluster> mean : meansOfClusters) {
                if(mean.getDocument().equals(document)) {
                    isCentroid = true;
                    break;
                }
            }
            if(isCentroid) {
                continue;
            }
            double maxSimilarity = -1.0;
            ClusterMean<Document, Cluster> mostRelevantCluster = null;
            for (ClusterMean<Document, Cluster> mean : meansOfClusters) {
                double similarity = utils.documentCosineSimilarity(document, mean.getDocument());
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                    mostRelevantCluster = mean;
                }
            }
            Cluster documentCluster = new Cluster(utils);
            for(Cluster cluster : allClusters) {
                if(cluster.getDocuments().contains(document)) {
                    documentCluster = cluster;
                    break;
                }
            }
            if (mostRelevantCluster.getCluster() != documentCluster) {
                documentCluster.getDocuments().remove(document);
                mostRelevantCluster.getCluster().getDocuments().add(document);
            }
        }

    }

    public void calculateNewCentroids(
            List<ClusterMean<Document, Cluster>> meansOfClusters
    ) {
        List<ClusterMean<Document, Cluster>> clonedList = new ArrayList<>(meansOfClusters);
        for (ClusterMean<Document, Cluster> clusterMean : clonedList) {
            Document newMeanDocument = clusterMean.getCluster().getMeanDocument();
            meansOfClusters.remove(clusterMean);
            meansOfClusters.add(new ClusterMean<>(newMeanDocument, clusterMean.getCluster()));
        }
    }
}
