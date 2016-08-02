package br.edu.ufcg.projetop1.entities;

/**
 * Created by Kelvin on 01/08/2016.
 */
public class Score {
    public Score(int score){
        this.score = score;
    }
    int score = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
