package be.kindengezin.groeipakket.commons.domain;

import be.kindengezin.groeipakket.commons.time.DateTimeProvider;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import static org.assertj.core.util.Preconditions.checkNotNull;

public class Metadata {

    private final Instant timestamp = DateTimeProvider.timestamp();

    private Principal principal;

    private Metadata() {
    }

    public Metadata(Principal principal) {
        checkNotNull(principal, "PRINCIPAL_IS_REQUIRED");
        this.principal = principal;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
    }

}
