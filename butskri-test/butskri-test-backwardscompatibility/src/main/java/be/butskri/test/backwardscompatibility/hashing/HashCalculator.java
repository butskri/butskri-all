package be.butskri.test.backwardscompatibility.hashing;

import static be.butskri.test.backwardscompatibility.hashing.HashCalculatorBuilder.calculationOf;
import static be.butskri.test.backwardscompatibility.hashing.Md5HashingAlgorithm.md5Hash;
import static be.butskri.test.backwardscompatibility.inforesolver.BytecodeOfClass.bytecode;

public interface HashCalculator {

    static HashCalculator defaultHashCalculator() {
        return calculationOf(md5Hash()).on(bytecode());
    }

    String hashFor(Class<?> clazz);

}
