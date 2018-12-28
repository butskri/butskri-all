package be.butskri.playground.java.xpath;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class XPathUtils {

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
    private static final XPathFactory X_PATH_FACTORY = XPathFactory.newInstance();

    public static List<String> extractValues(String xml, String xpathExpression) {
        Document doc = parse(xml);
        NodeList nodes = applyXpath(doc, xpathExpression);
        List<String> result = new ArrayList<>();
        for (int idx = 0; idx < nodes.getLength(); idx++) {
            Node node = nodes.item(idx);
            result.add(node.getTextContent());
        }
        return result;
    }

    private static Document parse(String xml) {
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes(Charset.defaultCharset()));
            return DOCUMENT_BUILDER_FACTORY.newDocumentBuilder().parse(is);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static NodeList applyXpath(Document doc, String xpathExpression) {
        try {
            XPath xpath = X_PATH_FACTORY.newXPath();
            NodeList nodes = (NodeList) xpath.evaluate(xpathExpression, doc, XPathConstants.NODESET);
            return nodes;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
