package com.github.sparkletarantula.processors.xml.utils;

import org.apache.nifi.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XMLParserXPath {

    public static String getValuesByXPathExpression(Document document, String expression) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression xPathExpression = xPath.compile(expression);
        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        List<String> results = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            results.add(getTextFromNode(node));
        }
        results.removeAll(Collections.singleton(""));
        return StringUtils.join(results, ",");

    }

    public static String getTextFromNode(Node node) {
        if (!node.hasChildNodes()) return "";
        StringBuilder stringBuilder = new StringBuilder();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node subnode = nodeList.item(i);
            if (subnode.getNodeType() == Node.TEXT_NODE) {
                stringBuilder.append(subnode.getTextContent().trim());
            }
        }
        return stringBuilder.toString();
    }
}
