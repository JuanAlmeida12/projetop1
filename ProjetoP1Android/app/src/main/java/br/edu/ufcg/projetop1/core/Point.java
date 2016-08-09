package br.edu.ufcg.projetop1.core;

import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 31/07/16.
 */
@IgnoreExtraProperties
public class Point {

    public Map<String, String> description;

    public Double lat;

    public Double lng;

    public String placeName;

    public List<String> tags;

    public int score;


    public Point() {

    }

    public Point(Map<String, String> description, Double lat, Double lng, String placeName, List<String> tags, int score) {
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.placeName = placeName;
        this.tags = tags;
        this.score = score;
    }
}
