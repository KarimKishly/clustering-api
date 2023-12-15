package com.clustering.hierarchical;

import java.util.Comparator;

public class ClusterComparator<T extends ClusterSimilarity<?, ?, Double>> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        return Double.compare(o2.getSimilarity(), o1.getSimilarity());
    }
}
