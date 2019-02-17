package be.butskri.playground.keng;

import be.butskri.playground.keng.myservice.beans.*;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;

import static java.nio.charset.Charset.forName;
import static org.assertj.core.api.Assertions.assertThat;

public class RandomBeanTest {

    private EnhancedRandom enhancedRandom;

    @Before
    public void setUp() {
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .randomizationDepth(3)
                .charset(forName("UTF-8"))
                .stringLengthRange(5, 50)
                .collectionSizeRange(2, 2)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(true)
                .build();
    }

    @Test
    public void generatedStandardJavaFieldsOfSomeBeanAreAllFilledIn() {
        SomeBean someBean = enhancedRandom.nextObject(SomeBean.class);

        assertThat(someBean.getInss()).isNotNull();
        assertThat(someBean.getLocalDate()).isNotNull();
        assertThat(someBean.getLocalDateTime()).isNotNull();
        assertThat(someBean.getSomeInt()).isNotNull();
        assertThat(someBean.getSomeInteger()).isNotNull();
        assertThat(someBean.getSomeString()).isNotNull();
        assertThat(someBean.getSomeUuid()).isNotNull();
        assertThat(someBean.getYearMonth()).isNotNull();
    }

    @Test
    public void listOfStringsIsRandomized() {
        SomeBean someBean = enhancedRandom.nextObject(SomeBean.class);

        assertThat(someBean.getSomeListOfStrings()).hasSize(2);
        assertThat(someBean.getSomeListOfStrings()).doesNotContain(new String[]{null});
    }

    @Test
    public void listOfSuperBeansIsRandomized() {
        SomeBean someBean = enhancedRandom.nextObject(SomeBean.class);

        assertThat(someBean.getSuperBeans()).hasSize(2);
        someBean.getSuperBeans().stream().forEach(this::assertValidSuperBean);
    }

    @Test
    public void nestedBeanIsRandomized() {
        SomeBean someBean = enhancedRandom.nextObject(SomeBean.class);

        NestedBean nestedBean = someBean.getNestedBean();
        assertThat(nestedBean).isNotNull();
        assertThat(nestedBean.getSomething()).isNotNull();
    }

    private void assertValidSuperBean(SuperBean superBean) {
        assertThat(superBean.getSuperProperty()).isNotNull();
        if (SubBeanOne.class.isInstance(superBean)) {
            assertThat(superBean).isInstanceOf(SubBeanOne.class);
            SubBeanOne subBeanOne = (SubBeanOne) superBean;
            assertThat(subBeanOne.getSubPropertyOne()).isNotNull();
        } else {
            assertThat(superBean).isInstanceOf(SubBeanTwo.class);
            SubBeanTwo subBean = (SubBeanTwo) superBean;
            assertThat(subBean.getSubPropertyTwo()).isNotNull();
        }
    }
}
