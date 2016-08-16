package br.edu.ufcg.projetop1.core;

import java.util.UUID;

/**
 * Created by root on 16/08/16.
 */
public class Badge implements Comparable<Badge> {

    private String name;
    private int meta;
    private int atual;
    private String description;
    private boolean pointsAdded;
    private String uuid;

    public Badge(String name, int meta, int atual, String description) {
        this.name = name;
        this.meta = meta;
        this.atual = atual;
        this.description = description;
        this.uuid = UUID.randomUUID().toString();
        pointsAdded = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMeta() {
        return meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }

    public int getAtual() {
        return atual;
    }

    public void setAtual(int atual) {
        this.atual = atual;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isPointsAdded() {
        return pointsAdded;
    }

    public void setPointsAdded(boolean pointsAdded) {
        this.pointsAdded = pointsAdded;
    }

    public void increment() {
        this.atual++;
    }

    public boolean badgeCompleted() {
        if (atual >= meta) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Badge o) {
        if (badgeCompleted() && !o.badgeCompleted()) {
            return -1;
        } else if (atual / meta > o.atual / o.meta) {
            return -1;
        } else if (atual / meta == o.atual / o.meta) {
            return 0;
        }
        return 1;
    }
}
