package com.clustering.vectorization;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class Collection {
    private List<Document> documents;
    private HashMap<String, HashMap<String, Integer>> corpusDocumentFreq;

    public Collection(boolean isTiny) {
        String data = "data";
        if(isTiny) {
            data = "data_tiny";
        }
        documents = initDocuments(data);
        corpusDocumentFreq = new HashMap<>();
        calculateIndexes();
    }

    public void calculateIndexes() {
        try {
            generateTfVectors();
            generateIdfVectors();
            generateTfIdfVectors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int size() {
        return documents.size();
    }

    private List<Document> initDocuments(String data) {
        Gson gsonObject = new Gson();
        try (BufferedReader reader = new BufferedReader(
                new FileReader("src/main/resources/" + data + ".json"))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            List<Document> myObjects = List.of(gsonObject.fromJson(json.toString(), Document[].class));
            return myObjects;
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            e.printStackTrace();
        }
        return List.of(new Document());
    }

    private void generateTfVectors() throws IOException {
        for (Document document : documents) {
            HashMap<String, Double> nameCorpus = new HashMap<>();
            HashMap<String, Double> japaneseNameCorpus = new HashMap<>();
            HashMap<String, Double> typeCorpus = new HashMap<>();
            HashMap<String, Double> episodesCorpus = new HashMap<>();
            HashMap<String, Double> studioCorpus = new HashMap<>();
            HashMap<String, Double> releaseSeasonCorpus = new HashMap<>();
            HashMap<String, Double> tagsCorpus = new HashMap<>();
            HashMap<String, Double> ratingCorpus = new HashMap<>();
            HashMap<String, Double> releaseYearCorpus = new HashMap<>();
            HashMap<String, Double> descriptionCorpus = new HashMap<>();
            HashMap<String, Double> contentWarningCorpus = new HashMap<>();
            HashMap<String, Double> relatedMangeCorpus = new HashMap<>();
            HashMap<String, Double> relatedAnimeCorpus = new HashMap<>();
            HashMap<String, Double> voiceActorsCorpus = new HashMap<>();
            HashMap<String, Double> staffCorpus = new HashMap<>();


            for (String word : document.getName().split(" ")) {
                nameCorpus.put(word.toLowerCase(), nameCorpus.getOrDefault(word, 0.0) + 1);
            }

            for (String word : document.getJapanese_name().split(" ")) {
                japaneseNameCorpus.put(word.toLowerCase(), japaneseNameCorpus.getOrDefault(word, 0.0) + 1);
            }

            for (String word : Lucene.removeStopwordsAndStem(document.getType()).split(" ")) {
                typeCorpus.put(word.toLowerCase(), typeCorpus.getOrDefault(word, 0.0) + 1);
            }

            episodesCorpus.put(document.getEpisodes(),
                    episodesCorpus.getOrDefault(document.getEpisodes(), 0.0) + 1);

            for (String word : document.getStudio().split(" ")) {
                studioCorpus.put(word.toLowerCase(), studioCorpus.getOrDefault(word, 0.0) + 1);
            }

            releaseSeasonCorpus.put(document.getRelease_season(),
                    releaseSeasonCorpus.getOrDefault(document.getRelease_season(), 0.0) + 1);

            for (String word : Lucene.removeStopwordsAndStem(document.getTags()).split(" ")) {
                tagsCorpus.put(word, tagsCorpus.getOrDefault(word, 0.0) + 1);
            }
            ratingCorpus.put(document.getRating(),
                    ratingCorpus.getOrDefault(document.getRating(), 0.0) + 1);

            releaseYearCorpus.put(document.getRelease_year(),
                    releaseYearCorpus.getOrDefault(document.getRelease_year(), 0.0) + 1);

            for (String word : Lucene.removeStopwordsAndStem(document.getDescription()).split(" ")) {
                descriptionCorpus.put(word, descriptionCorpus.getOrDefault(word, 0.0) + 1);
            }
            for (String word : Lucene.removeStopwordsAndStem(document.getContent_Warning()).split(" ")) {
                contentWarningCorpus.put(word, contentWarningCorpus.getOrDefault(word, 0.0) + 1);
            }

            for (String word : document.getRelated_Mange().split(" ")) {
                relatedMangeCorpus.put(word.toLowerCase(), relatedMangeCorpus.getOrDefault(word, 0.0) + 1);
            }

            for (String word : document.getRelated_anime().split(" ")) {
                relatedAnimeCorpus.put(word.toLowerCase(), relatedAnimeCorpus.getOrDefault(word, 0.0) + 1);
            }

            for (String word : document.getRelated_anime().split(" ")) {
                relatedAnimeCorpus.put(word.toLowerCase(), relatedAnimeCorpus.getOrDefault(word, 0.0) + 1);
            }

            for (String word : Lucene.removeStopwordsAndStem(document.getVoice_actors()).split(" ")) {
                voiceActorsCorpus.put(word.toLowerCase(), voiceActorsCorpus.getOrDefault(word, 0.0) + 1);
            }

            for (String word : Lucene.removeStopwordsAndStem(document.getStaff()).split(" ")) {
                staffCorpus.put(word.toLowerCase(), staffCorpus.getOrDefault(word, 0.0) + 1);
            }

            document.getVectorsMap().put("name", nameCorpus);
            document.getVectorsMap().put("japaneseName", japaneseNameCorpus);
            document.getVectorsMap().put("type", typeCorpus);
            document.getVectorsMap().put("episodes", episodesCorpus);
            document.getVectorsMap().put("studio", studioCorpus);
            document.getVectorsMap().put("releaseSeason", releaseSeasonCorpus);
            document.getVectorsMap().put("tags", tagsCorpus);
            document.getVectorsMap().put("rating", ratingCorpus);
            document.getVectorsMap().put("releaseYear", releaseYearCorpus);
            document.getVectorsMap().put("description", descriptionCorpus);
            document.getVectorsMap().put("contentWarning", contentWarningCorpus);
            document.getVectorsMap().put("relatedMange", relatedMangeCorpus);
            document.getVectorsMap().put("relatedAnime", relatedAnimeCorpus);
            document.getVectorsMap().put("voiceActors", voiceActorsCorpus);
            document.getVectorsMap().put("staff", staffCorpus);

        }
    }

    private void generateIdfVectors() throws IOException {
        HashMap<String, Integer> nameCorpus = new HashMap<>();
        HashMap<String, Integer> japaneseNameCorpus = new HashMap<>();
        HashMap<String, Integer> typeCorpus = new HashMap<>();
        HashMap<String, Integer> episodesCorpus = new HashMap<>();
        HashMap<String, Integer> studioCorpus = new HashMap<>();
        HashMap<String, Integer> releaseSeasonCorpus = new HashMap<>();
        HashMap<String, Integer> tagsCorpus = new HashMap<>();
        HashMap<String, Integer> ratingCorpus = new HashMap<>();
        HashMap<String, Integer> releaseYearCorpus = new HashMap<>();
        HashMap<String, Integer> descriptionCorpus = new HashMap<>();
        HashMap<String, Integer> contentWarningCorpus = new HashMap<>();
        HashMap<String, Integer> relatedMangeCorpus = new HashMap<>();
        HashMap<String, Integer> relatedAnimeCorpus = new HashMap<>();
        HashMap<String, Integer> voiceActorsCorpus = new HashMap<>();
        HashMap<String, Integer> staffCorpus = new HashMap<>();

        for (Document document : documents) {
            for (String word : document.getName().split(" ")) {
                nameCorpus.put(word.toLowerCase(), nameCorpus.getOrDefault(word, 0) + 1);
            }

            for (String word : document.getJapanese_name().split(" ")) {
                japaneseNameCorpus.put(word.toLowerCase(), japaneseNameCorpus.getOrDefault(word, 0) + 1);
            }

            for (String word : Lucene.removeStopwordsAndStem(document.getType()).split(" ")) {
                typeCorpus.put(word.toLowerCase(), typeCorpus.getOrDefault(word, 0) + 1);
            }

            episodesCorpus.put(document.getEpisodes(),
                    episodesCorpus.getOrDefault(document.getEpisodes(), 0) + 1);

            for (String word : document.getStudio().split(" ")) {
                studioCorpus.put(word.toLowerCase(), studioCorpus.getOrDefault(word, 0) + 1);
            }

            releaseSeasonCorpus.put(document.getRelease_season(),
                    releaseSeasonCorpus.getOrDefault(document.getRelease_season(), 0) + 1);

            for (String word : Lucene.removeStopwordsAndStem(document.getTags()).split(" ")) {
                tagsCorpus.put(word, tagsCorpus.getOrDefault(word, 0) + 1);
            }
            ratingCorpus.put(document.getRating(),
                    ratingCorpus.getOrDefault(document.getRating(), 0) + 1);

            releaseYearCorpus.put(document.getRelease_year(),
                    releaseYearCorpus.getOrDefault(document.getRelease_year(), 0) + 1);

            for (String word : Lucene.removeStopwordsAndStem(document.getDescription()).split(" ")) {
                descriptionCorpus.put(word, descriptionCorpus.getOrDefault(word, 0) + 1);
            }

            for (String word : Lucene.removeStopwordsAndStem(document.getContent_Warning()).split(" ")) {
                contentWarningCorpus.put(word, contentWarningCorpus.getOrDefault(word, 0) + 1);
            }

            for (String word : document.getRelated_Mange().split(" ")) {
                relatedMangeCorpus.put(word.toLowerCase(), relatedMangeCorpus.getOrDefault(word, 0) + 1);
            }

            for (String word : document.getRelated_anime().split(" ")) {
                relatedAnimeCorpus.put(word.toLowerCase(), relatedAnimeCorpus.getOrDefault(word, 0) + 1);
            }

            for (String word : document.getRelated_anime().split(" ")) {
                relatedAnimeCorpus.put(word.toLowerCase(), relatedAnimeCorpus.getOrDefault(word, 0) + 1);
            }

            for (String word : Lucene.removeStopwordsAndStem(document.getVoice_actors()).split(" ")) {
                voiceActorsCorpus.put(word.toLowerCase(), voiceActorsCorpus.getOrDefault(word, 0) + 1);
            }

            for (String word : Lucene.removeStopwordsAndStem(document.getStaff()).split(" ")) {
                staffCorpus.put(word.toLowerCase(), staffCorpus.getOrDefault(word, 0) + 1);
            }


        }
        corpusDocumentFreq.put("name", nameCorpus);
        corpusDocumentFreq.put("japaneseName", japaneseNameCorpus);
        corpusDocumentFreq.put("type", typeCorpus);
        corpusDocumentFreq.put("episodes", episodesCorpus);
        corpusDocumentFreq.put("studio", studioCorpus);
        corpusDocumentFreq.put("releaseSeason", releaseSeasonCorpus);
        corpusDocumentFreq.put("tags", tagsCorpus);
        corpusDocumentFreq.put("rating", ratingCorpus);
        corpusDocumentFreq.put("releaseYear", releaseYearCorpus);
        corpusDocumentFreq.put("description", descriptionCorpus);
        corpusDocumentFreq.put("contentWarning", contentWarningCorpus);
        corpusDocumentFreq.put("relatedMange", relatedMangeCorpus);
        corpusDocumentFreq.put("relatedAnime", relatedAnimeCorpus);
        corpusDocumentFreq.put("voiceActors", voiceActorsCorpus);
        corpusDocumentFreq.put("staff", staffCorpus);

    }

    private void generateTfIdfVectors() {
        for (Document document : documents) {
            for (String attribute : document.getVectorsMap().keySet()) {
                for (String vectorElement : document.getVectorsMap().get(attribute).keySet()) {
                    document.getVectorsMap().get(attribute).put(
                            vectorElement,
                            document.getVectorsMap().get(attribute).get(vectorElement) *
                                    Math.log((double) documents.size() / corpusDocumentFreq.get(attribute).get(vectorElement))
                    );
                }
            }
        }
    }
}
