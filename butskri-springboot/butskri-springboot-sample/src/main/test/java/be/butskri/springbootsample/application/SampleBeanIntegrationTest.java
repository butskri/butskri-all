package be.butskri.springbootsample.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {SpringSampleApplication.class}
        , initializers = ConfigFileApplicationContextInitializer.class
)
@ActiveProfiles("test")
public class SampleBeanIntegrationTest {

    @Autowired
    private SampleBean sampleBean;

    @Test
    public void valueIsSetFromApplicationYmlFileForTestProfile() {
        assertThat(sampleBean.getValue()).isEqualTo("testValue");
    }
}
