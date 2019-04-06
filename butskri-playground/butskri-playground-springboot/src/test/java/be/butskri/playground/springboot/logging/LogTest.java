package be.butskri.playground.springboot.logging;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class LogTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTest.class);
    public static final int NUMBER_OF_LOGMESSAGES = 1000;

    @Ignore("Test ignored because of its only purpose is to generate a lot of logs for testing the ELK stack. Rename the file _logback-spring.xml if you want to enable logging again")
    @Test
    public void generateLotsOfLogs() {
        for (int i = 0; i < NUMBER_OF_LOGMESSAGES; i++) {
            randomInfoMessage();
            anotherRandomInfoMessage();
            logError();
        }
    }

    public void randomInfoMessage() {
        LOGGER.info("Some random message with a uuid {}", UUID.randomUUID());
    }

    public void anotherRandomInfoMessage() {
        LOGGER.info("Yet another random info message", UUID.randomUUID());
    }

    public void logError() {
        String randomMessage = randomErrorMessage();
        String msg = String.format(randomMessage, UUID.randomUUID().toString());
        LOGGER.error(msg, randomException(msg));
    }

    public IllegalArgumentException randomException(String msg) {
        return new IllegalArgumentException(msg);
    }

    public String randomErrorMessage() {
        int random = new Random().nextInt() % 5;
        switch (random) {
            case 0:
                return "strange exception occured for person with id %S";
            case 1:
                return "person id %s not found";
            case 2:
                return "Trouble handling person %s";
            case 3:
                return "User %s did something really wrong";
            case 4:
                return "Nobody ever expected %s would happen";
        }
        return "Yet some other %s kind of exception";
    }
}
