package be.kindengezin.groeipakket.backwardscompatibility.json.random;

import be.kindengezin.groeipakket.commons.domain.Metadata;
import be.kindengezin.groeipakket.commons.domain.Principal;

import java.util.function.Supplier;

public class MetadataRandomizer implements Supplier<Metadata> {

    @Override
    public Metadata get() {
        return new Metadata(new Principal("principalId", "principalType"));
    }
}
