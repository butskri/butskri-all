package be.butskri.playground.java.xpath;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class XPathUtilsTest {

    @Test
    public void canExtractSingleValueFromXml() {
        List<String> result = XPathUtils.extractValues("<foo>" +
                        "<anotherBar>jdfkqj</anotherBar>" +
                        "<bar>something</bar>" +
                        "<anotherFoo>hereWeGoo</anotherFoo>" +
                        "</foo>",
                "//foo/bar");

        assertThat(result).containsExactly("something");
    }

    @Test
    public void canExtractMultipleValuesFromXml() {
        List<String> result = XPathUtils.extractValues("<foo>" +
                        "<anotherBar>jdfkqj</anotherBar>" +
                        "<bar>something</bar>" +
                        "<anotherFoo>hereWeGoo</anotherFoo>" +
                        "<bar>somethingElse</bar>" +
                        "</foo>",
                "//foo/bar");

        assertThat(result).containsExactly("something", "somethingElse");
    }
}
