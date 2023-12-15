package com.clustering.utils;

import com.clustering.http.HierarchicalRequest;
import com.clustering.http.KMeansRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.clustering.vectorization.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {
    private double nameWeight; private double japaneseNameWeight; private double typeWeight;
    private double episodesWeight; private double studioWeight;
    private double releaseSeasonWeight; private double tagsWeight;
    private double ratingWeight; private double releaseYearWeight;
    private double descriptionWeight; private double contentWarningWeight;
    private double relatedMangeWeight; private double relatedAnimeWeight;
    private double voiceActorsWeight; private double staffWeight;
    public Utils(HierarchicalRequest request) {
        nameWeight = request.getNameWeight();
        japaneseNameWeight = request.getJapaneseNameWeight();
        typeWeight = request.getTypeWeight();
        episodesWeight = request.getEpisodesWeight();
        studioWeight = request.getStudioWeight();
        releaseSeasonWeight = request.getReleaseSeasonWeight();
        tagsWeight = request.getTagsWeight();
        ratingWeight = request.getRatingWeight();
        releaseYearWeight = request.getReleaseYearWeight();
        descriptionWeight = request.getDescriptionWeight();
        contentWarningWeight = request.getContentWarningWeight();
        relatedMangeWeight = request.getRelatedMangeWeight();
        relatedAnimeWeight = request.getRelatedAnimeWeight();
        voiceActorsWeight = request.getVoiceActorsWeight();
        staffWeight = request.getStaffWeight();
    }
    public Utils(KMeansRequest request) {
        nameWeight = request.getNameWeight();
        japaneseNameWeight = request.getJapaneseNameWeight();
        typeWeight = request.getTypeWeight();
        episodesWeight = request.getEpisodesWeight();
        studioWeight = request.getStudioWeight();
        releaseSeasonWeight = request.getReleaseSeasonWeight();
        tagsWeight = request.getTagsWeight();
        ratingWeight = request.getRatingWeight();
        releaseYearWeight = request.getReleaseYearWeight();
        descriptionWeight = request.getDescriptionWeight();
        contentWarningWeight = request.getContentWarningWeight();
        relatedMangeWeight = request.getRelatedMangeWeight();
        relatedAnimeWeight = request.getRelatedAnimeWeight();
        voiceActorsWeight = request.getVoiceActorsWeight();
        staffWeight = request.getStaffWeight();
    }

    public double interClusterSimilarity(Cluster cluster1, Cluster cluster2) {
        double similarity = 0;
        for (Document document1 : cluster1.getDocuments()) {
            for (Document document2 : cluster2.getDocuments()) {
                similarity += documentCosineSimilarity(document1, document2);
            }
        }
        similarity = similarity / ((cluster1.size() + cluster2.size()) * (cluster1.size() + cluster2.size() - 1));

        return similarity;
    }

    public double getTotalSimilarity() {
        double totalSimilarity = nameWeight + japaneseNameWeight + typeWeight + episodesWeight
                + studioWeight + releaseSeasonWeight + tagsWeight + ratingWeight + releaseYearWeight
                + descriptionWeight + contentWarningWeight + relatedMangeWeight + relatedAnimeWeight
                + voiceActorsWeight + staffWeight;
        return totalSimilarity;
    }

    public double documentCosineDistance(Document document1, Document document2) {
        double totalSimilarity = getTotalSimilarity();
        System.out.println(totalSimilarity);
        System.out.println(totalSimilarity - documentCosineSimilarity(document1, document2));
        return totalSimilarity - documentCosineSimilarity(document1, document2);

    }

    public double documentCosineSimilarity(Document document1, Document document2) {
        HashMap<String, Double> elementSimilarity = new HashMap<>();

        for (String element : document1.getVectorsMap().keySet()) {
            elementSimilarity.put(element, elementCosineSimilarity(
                    element,
                    document1,
                    document2
            ));
        }
        return
                elementSimilarity.get("name") * nameWeight
                        + elementSimilarity.get("japaneseName") * japaneseNameWeight
                        + elementSimilarity.get("type") * typeWeight
                        + elementSimilarity.get("episodes") * episodesWeight
                        + elementSimilarity.get("studio") * studioWeight
                        + elementSimilarity.get("releaseSeason") * releaseSeasonWeight
                        + elementSimilarity.get("tags") * tagsWeight
                        + elementSimilarity.get("rating") * ratingWeight
                        + elementSimilarity.get("releaseYear") * releaseYearWeight
                        + elementSimilarity.get("description") * descriptionWeight
                        + elementSimilarity.get("contentWarning") * contentWarningWeight
                        + elementSimilarity.get("relatedMange") * relatedMangeWeight
                        + elementSimilarity.get("relatedAnime") * relatedAnimeWeight
                        + elementSimilarity.get("voiceActors") * voiceActorsWeight
                        + elementSimilarity.get("staff") * staffWeight;
    }

    public static double elementCosineSimilarity(String element, Document document1, Document document2) {
        HashMap<String, Double> element1 = document1.getVectorsMap().get(element);
        HashMap<String, Double> element2 = document2.getVectorsMap().get(element);
        if (element2 == null) {
            return 0.0;
        }
        double numerator = 0;
        double denominator1 = 0;
        for (String token : element1.keySet()) {
            numerator = numerator + (element1.get(token) * element2.getOrDefault(token, 0.0));
            denominator1 = denominator1 + Math.pow(element1.get(token), 2);
        }
        denominator1 = Math.sqrt(denominator1);
        double denominator2 = 0;
        for (String token : element2.keySet()) {
            denominator2 = denominator2 + Math.pow(element2.get(token), 2);
        }
        denominator2 = Math.sqrt(denominator2);
        if (numerator == 0 && denominator1 == 0 && denominator2 == 0) return 0;
        return numerator / (denominator1 * denominator2);
    }

    public static void populateDendrogram(List<List<List<Integer>>> dendrogram, List<Cluster> clustersList) {
        List<List<Integer>> hashesPerIteration = new ArrayList<>();
        for(Cluster cluster : clustersList) {
            List<Integer> hashes = new ArrayList<>();
            for(Document document : cluster.getDocuments()) {
                hashes.add(document.hashCode());
            }
            hashesPerIteration.add(hashes);
        }
        dendrogram.add(hashesPerIteration);
    }

    public double daviesBouldinIndex(List<Cluster> clustersList) {
        double index = 0.0;
        List<Double> avgDistances = new ArrayList<>();
        for(int i = 0; i < clustersList.size(); i++) {
            double avgDistance = 0;
            Document centroid = clustersList.get(i).getMeanDocument();
            for(Document document : clustersList.get(i).getDocuments()) {
                avgDistance += documentCosineSimilarity(document, centroid)/clustersList.size();
            }
            avgDistances.add(avgDistance);
        }
        for(int i = 0; i < clustersList.size(); i++) {
            double iAvgDistance = avgDistances.get(i);
            Document iCentroid = clustersList.get(i).getMeanDocument();
            double maxAvg = 0;
            for(int j = 0; j < clustersList.size(); j++) {
                double jAvgDistance = avgDistances.get(j);
                Document jCentroid = clustersList.get(j).getMeanDocument();
                if(i != j) {
                    double newAvg = (iAvgDistance+jAvgDistance)/documentCosineDistance(iCentroid, jCentroid);
                    maxAvg = Math.max(maxAvg, newAvg);
                }
            }
            index += maxAvg;
        }
        return index;
    }

    public double dunnIndex(List<Cluster> clustersList, Utils utils) {
        double maxIntraDistance = getTotalSimilarity() - clustersList.stream()
                .mapToDouble(cluster -> new Cluster(utils).getIntraClusterSimilarity()).min().getAsDouble();
        double minInterDistance = getTotalSimilarity();
        for(int i = 0; i < clustersList.size() - 1; i++) {
            for(int j = i+1; j < clustersList.size(); j++) {
                double interDistance = getTotalSimilarity() -
                        interClusterSimilarity(clustersList.get(i), clustersList.get(j));
                minInterDistance = Math.min(minInterDistance, interDistance);
            }
        }
        return minInterDistance/maxIntraDistance;
    }

    public static void writeKResults(List<Cluster> resultingCluster) throws IOException {
        String resourcesPath = "src/main/resources/";
        int trialNumber = getLastTrial();
        String trialPath = resourcesPath + "trial_" + trialNumber;
        Files.createDirectories(Path.of(trialPath));
        for (int i = 0; i < resultingCluster.size(); i++) {
            Cluster currentCluster = resultingCluster.get(i);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(trialPath + "/cluster_" + (i + 1) + ".json")) {
                gson.toJson(currentCluster.getDocuments(), writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void writeResults(List<Cluster> resultingCluster) throws IOException {
        String resourcesPath = "src/main/resources/";
        int trialNumber = getLastTrial();
        String trialPath = resourcesPath + "trial_" + trialNumber;
        Files.createDirectories(Path.of(trialPath));
        for (int i = 0; i < resultingCluster.size(); i++) {
            Cluster currentCluster = resultingCluster.get(i);
            if (currentCluster.size() > 1) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                try (FileWriter writer = new FileWriter(trialPath + "/cluster_" + (i + 1) + ".json")) {
                    gson.toJson(currentCluster.getDocuments(), writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int getLastTrial() {
        String resourcesPath = "src/main/resources/";
        File directory = new File(resourcesPath);
        File[] files = directory.listFiles();
        int lastTrial = 0;
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (file.getName().startsWith("trial_")) {
                        int currentTrial = Integer.parseInt(file.getName().substring(file.getName().indexOf('_') + 1));
                        if (currentTrial > lastTrial) {
                            lastTrial = currentTrial;
                        }
                    }
                }
            }
        }
        return lastTrial + 1;
    }
}
