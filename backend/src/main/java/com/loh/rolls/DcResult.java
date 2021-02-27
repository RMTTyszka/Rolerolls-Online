package com.loh.rolls;

public class DcResult {
    public Integer difficulty;
    public Integer complexity;
    public Double chance;

    public DcResult(Integer difficulty, Integer complexity, Double chance) {
        this.difficulty = difficulty;
        this.complexity = complexity;
        this.chance = (double) Math.round(chance * 100) / 100;
    }
}
