package com.clustering.vectorization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.HashMap;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@ToString
public class Document {
    private String Rank;
    private String Name;
    private String Japanese_name;
    private String Type;
    private String Episodes;
    private String Studio;
    private String Release_season;
    private String Tags;
    private String Rating;
    private String Release_year;
    private String Description;
    private String Content_Warning;
    private String Related_Mange;
    private String Related_anime;
    private String Voice_actors;
    private String staff;

    @JsonIgnore
    private HashMap<String, HashMap<String, Double>> vectorsMap = new HashMap<>();


}
