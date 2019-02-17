package be.butskri.playground.keng;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SubBeanOne.class, name = "SUB_BEAN_ONE"),
        @JsonSubTypes.Type(value = SubBeanTwo.class, name = "SUB_BEAN_TWO")})
public class SuperBeanMixin {
}
