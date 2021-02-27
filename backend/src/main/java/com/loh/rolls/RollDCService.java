package com.loh.rolls;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RollDCService {

    private DiceRoller roller = new DiceRoller();
    private Integer difficultRange = 50;
    private Integer complexityRange = 10;
    private Double numberOfRolls = 1000d;
    private Integer standardDeviation = 5;

    public List<DcResult> getDC(Integer points, Integer bonus, Double chance) {

        List<DcResult> results = new ArrayList();
        for (int difficulty = 1; difficulty < difficultRange; difficulty++) {
            for (int complexity = 1; complexity < complexityRange; complexity++) {
                Integer currentSuccesses = 0;
                for (int rolls = 0; rolls < numberOfRolls; rolls++) {
                    boolean success = getChance(points, bonus, difficulty, complexity);
                    if (success) {
                        currentSuccesses++;
                    }
                }
                Double currentChance = currentSuccesses / numberOfRolls * 100;
                if (currentChance - chance >= -standardDeviation && currentChance - chance <= standardDeviation) {
                    results.add(new DcResult(difficulty, complexity, currentChance));
                }
            }
        }

        return results;
    }
    public Double verifyDC(Integer points, Integer bonus, Integer difficulty, Integer complexity) {
        Double chance = 0d;

        for (int rolls = 0; rolls < numberOfRolls; rolls++) {
            chance += roller.rollTest(points, bonus, difficulty, complexity, false).isSuccess() ? 1 : 0;
        }

        return chance / numberOfRolls * 100;
    }

    private boolean getChance(Integer points, Integer bonus, Integer difficulty, Integer complexity) {
        RollTestResult roll = roller.rollTest(points, bonus, difficulty, complexity, false);
        return roll.isSuccess();
    }
}

