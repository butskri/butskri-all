package be.butskri.test.backwardscompatibility.hashing;

import be.butskri.test.backwardscompatibility.inforesolver.DataOfClass;

public class HashCalculatorBuilder {

    private HashingAlgorithm hashingAlgorithm;

    public static HashCalculatorBuilder calculationOf(HashingAlgorithm hashingAlgorithm) {
        return new HashCalculatorBuilder(hashingAlgorithm);
    }

    private HashCalculatorBuilder(HashingAlgorithm hashingAlgorithm) {
        this.hashingAlgorithm = hashingAlgorithm;
    }

    public HashCalculator on(DataOfClass... dataOfClass) {
        return new SimpleHashCalculator(hashingAlgorithm, dataOfClass);
    }
}
