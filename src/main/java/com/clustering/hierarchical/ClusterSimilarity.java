package com.clustering.hierarchical;

public class ClusterSimilarity<A, B, C> {
    private final A cluster1;
    private final B cluster2;
    private final C similarity;
    private String id;

    public ClusterSimilarity(A cluster1, B cluster2, C similarity) {
        this.cluster1 = cluster1;
        this.cluster2 = cluster2;
        this.similarity = similarity;
        id = generateID();
    }

    public A getFirstCluster() {
        return cluster1;
    }
    public B getSecondCluster() {
        return cluster2;
    }
    public C getSimilarity() {
        return similarity;
    }
    public String getId() {
        return id;
    }
    private String generateID() {
        return cluster1.hashCode() +String.valueOf(cluster2.hashCode());
    }

    public String toString() {
        return getFirstCluster().toString() + "\n"
                + getSecondCluster().toString() + "\n"
                + getSimilarity();
    }
 }
