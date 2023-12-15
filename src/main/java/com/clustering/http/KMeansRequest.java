package com.clustering.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KMeansRequest {
    private double nameWeight; private double japaneseNameWeight; private double typeWeight;
    private double episodesWeight; private double studioWeight;
    private double releaseSeasonWeight; private double tagsWeight;
    private double ratingWeight; private double releaseYearWeight;
    private double descriptionWeight; private double contentWarningWeight;
    private double relatedMangeWeight; private double relatedAnimeWeight;
    private double voiceActorsWeight; private double staffWeight;

    private int k;
    private boolean isTiny;

}
