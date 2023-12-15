package com.clustering.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;
import com.clustering.vectorization.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ToString
public class Cluster {
    private List<Document> documents;
    @JsonIgnore
    private double intraClusterSimilarity;
    @JsonIgnore
    private Utils utils;

    public Cluster(Utils utils) {
        documents = new ArrayList<>();
        this.utils = utils;
    }
    public Cluster(Document document, Utils utils) {
        documents = new ArrayList<>();
        documents.add(document);
        intraClusterSimilarity = 0.0;
        this.utils = utils;
    }
    public Cluster(Cluster cluster1, Cluster cluster2, Utils utils) {
        documents = new ArrayList<>();
        documents.addAll(cluster1.getDocuments());
        documents.addAll(cluster2.getDocuments());
        this.utils = utils;
        intraClusterSimilarity = calculateIntraClusterSimilarity();
    }
    public Document getMeanDocument() {
        Document mean = new Document();
        documents.forEach(document -> {
            document.getVectorsMap().keySet().forEach(attribute -> {
                document.getVectorsMap().get(attribute).keySet().forEach(word -> {
                    HashMap<String, HashMap<String, Double>> vectorMap = mean.getVectorsMap();
                    HashMap<String, Double> dimensionMap = vectorMap.getOrDefault(attribute, new HashMap<>());
                    dimensionMap.put(word, dimensionMap.getOrDefault(word, 0.0)
                            + (document.getVectorsMap().get(attribute).get(word) / documents.size())
                    );
                    vectorMap.put(attribute, dimensionMap);
                });
            });
        });
        return mean;
    }
    private double calculateIntraClusterSimilarity() {
        double distance = 0.0;
        if(documents.isEmpty() || documents.size() == 1) {
            return 0.0;
        }
        Document mean = getMeanDocument();
        for(Document document : documents) {
            distance += Math.pow((utils.documentCosineDistance(document, mean)), 2);
        }
        double toReturn = distance / (documents.size() * (documents.size())-1);
        if(Double.isNaN(toReturn)) {
            return utils.getTotalSimilarity();
        }
        return toReturn;
    }
    public List<Document> getDocuments() {
        return documents;
    }
    public int size() {
        return documents.size();
    }
    public double getIntraClusterSimilarity() {
        return calculateIntraClusterSimilarity();
    }
}
