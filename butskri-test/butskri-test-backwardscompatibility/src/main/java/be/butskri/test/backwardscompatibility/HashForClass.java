package be.butskri.test.backwardscompatibility;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

class HashForClass implements Comparable<HashForClass> {

    private static final String SEPARATOR = "=";
    private String className;
    private String hash;

    static HashForClass fromLine(String line) {
        String[] split = line.split(SEPARATOR);
        String className = split[0];
        String hash = split[1];
        return new HashForClass(className, hash);
    }

    HashForClass(Class<?> clazz, String hash) {
        this(clazz.getName(), hash);
    }

    private HashForClass(String className, String hash) {
        this.className = className;
        this.hash = hash;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return toLine();
    }

    @Override
    public int compareTo(HashForClass other) {
        return this.toLine().compareTo(other.toLine());
    }

    String toLine() {
        return className + SEPARATOR + hash;
    }
}
