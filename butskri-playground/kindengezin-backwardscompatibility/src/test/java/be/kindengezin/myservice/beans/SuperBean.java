package be.kindengezin.myservice.beans;

import be.kindengezin.groeipakket.domain.read.ViewObject;

public abstract class SuperBean extends ViewObject {

    private String superProperty;

    public String getSuperProperty() {
        return superProperty;
    }
}
