package be.butskri.playground.keng.commons.backwardscompatibility.json.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClassMetadata {

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String correlationId;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dataSubjectId;
    private List<String> personalDataFields;
    private List<String> deepPersonalDataFields;

    private ClassMetadata() {
        // no-arg constructor for jackson
    }

    public ClassMetadata(String correlationId, String dataSubjectId, Collection<String> personalDataFields, Collection<String> deepPersonalDataFields) {
        this.correlationId = correlationId;
        this.dataSubjectId = dataSubjectId;
        this.personalDataFields = makeSortedList(personalDataFields);
        this.deepPersonalDataFields = makeSortedList(deepPersonalDataFields);
    }

    private List<String> makeSortedList(Collection<String> personalDataFields) {
        List<String> result = new ArrayList<>(personalDataFields);
        Collections.sort(result);
        return result;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getDataSubjectId() {
        return dataSubjectId;
    }

    public Collection<String> getPersonalDataFields() {
        return personalDataFields;
    }

    public Collection<String> getDeepPersonalDataFields() {
        return deepPersonalDataFields;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class Builder {

        private FieldInfo correlationIdInfo;
        private FieldInfo dataSubjectIdInfo;
        private Collection<FieldInfo> personalDataFieldsInfos;
        private Collection<FieldInfo> deepPersonalDataFieldsInfos;

        private Builder() {
        }

        public ClassMetadata build() {
            return new ClassMetadata(
                    fieldNameOf(correlationIdInfo),
                    fieldNameOf(dataSubjectIdInfo),
                    personalDataFieldsInfos.stream().map(FieldInfo::getFieldName).collect(Collectors.toSet()),
                    deepPersonalDataFieldsInfos.stream().map(Object::toString).collect(Collectors.toSet())
            );
        }

        public Builder withCorrelationIdInfo(FieldInfo correlationIdInfo) {
            this.correlationIdInfo = correlationIdInfo;
            return this;
        }

        public Builder withDataSubjectIdInfo(FieldInfo dataSubjectIdInfo) {
            this.dataSubjectIdInfo = dataSubjectIdInfo;
            return this;
        }

        public Builder withPersonalDataFieldsInfos(Collection<FieldInfo> personalDataFieldsInfos) {
            this.personalDataFieldsInfos = personalDataFieldsInfos;
            return this;
        }

        public Builder withDeepPersonalDataFieldsInfos(Collection<FieldInfo> deepPersonalDataFieldsInfos) {
            this.deepPersonalDataFieldsInfos = deepPersonalDataFieldsInfos;
            return this;
        }

        private String fieldNameOf(FieldInfo fieldInfo) {
            if (fieldInfo == null) {
                return null;
            }
            return fieldInfo.getFieldName();
        }

    }
}
