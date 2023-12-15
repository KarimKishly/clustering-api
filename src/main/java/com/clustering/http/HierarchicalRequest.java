package com.clustering.http;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HierarchicalRequest {
    private double nameWeight; private double japaneseNameWeight; private double typeWeight;
    private double episodesWeight; private double studioWeight;
    private double releaseSeasonWeight; private double tagsWeight;
    private double ratingWeight; private double releaseYearWeight;
    private double descriptionWeight; private double contentWarningWeight;
    private double relatedMangeWeight; private double relatedAnimeWeight;
    private double voiceActorsWeight; private double staffWeight;

    private boolean isTiny;
    private int decreases;

}
