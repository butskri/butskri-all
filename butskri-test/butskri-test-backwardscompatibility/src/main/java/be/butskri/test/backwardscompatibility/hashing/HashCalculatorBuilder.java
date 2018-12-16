package be.butskri.test.backwardscompatibility.hashing;

import be.butskri.test.backwardscompatibility.dataofclass.DataOfClass;

public interface HashCalculatorBuilder {

    HashCalculator on(DataOfClass... dataOfClass);

}
