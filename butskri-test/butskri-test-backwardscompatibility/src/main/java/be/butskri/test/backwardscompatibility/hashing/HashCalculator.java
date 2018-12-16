package be.butskri.test.backwardscompatibility.hashing;

import static be.butskri.test.backwardscompatibility.dataofclass.BytecodeOfClass.bytecode;
import static be.butskri.test.backwardscompatibility.hashing.Md5HashingAlgorithm.md5Hash;

public interface HashCalculator {

    static HashCalculatorBuilder calculationOf(HashingAlgorithm hashingAlgorithm) {
        return (dataOfClass) -> new SimpleHashCalculator(hashingAlgorithm, dataOfClass);
    }

    static HashCalculator defaultHashCalculator() {
        return calculationOf(md5Hash()).on(bytecode());
    }

    String hashFor(Class<?> clazz);
}
