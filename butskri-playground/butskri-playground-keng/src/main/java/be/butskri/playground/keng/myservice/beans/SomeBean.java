package be.butskri.playground.keng.myservice.beans;

import be.butskri.playground.keng.commons.domain.ViewObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class SomeBean extends ViewObject {

    private String someString;
    private int someInt;
    private Integer someInteger;
    private UUID someUuid;
    private List<String> someListOfStrings;
    private NestedBean nestedBean;
    private Collection<SuperBean> superBeans;
    private SimplifiedInss inss;
    private LocalDateTime localDateTime;
    private LocalDate localDate;
    private YearMonth yearMonth;

    private SomeBean() {
    }

    public String getSomeString() {
        return someString;
    }

    public int getSomeInt() {
        return someInt;
    }

    public Integer getSomeInteger() {
        return someInteger;
    }

    public UUID getSomeUuid() {
        return someUuid;
    }

    public List<String> getSomeListOfStrings() {
        return someListOfStrings;
    }

    public NestedBean getNestedBean() {
        return nestedBean;
    }

    public Collection<SuperBean> getSuperBeans() {
        return superBeans;
    }

    public SimplifiedInss getInss() {
        return inss;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }
}
