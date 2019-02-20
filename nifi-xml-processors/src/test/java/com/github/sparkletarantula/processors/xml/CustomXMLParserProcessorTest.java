/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.sparkletarantula.processors.xml;

import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class CustomXMLParserProcessorTest {

    private TestRunner testRunner;

    @Before
    public void init() {
        testRunner = TestRunners.newTestRunner(CustomXMLParserProcessor.class);
    }

    @Test
    public void testMultipleQueries() {
        InputStream content = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.xml");
        testRunner.enqueue(content);
        testRunner.setProperty("foo", "/root/foo");
        testRunner.setProperty("bar", "//bar");
        testRunner.setProperty("buzz", "/root/buzz");
        testRunner.run();
        testRunner.assertAllFlowFilesTransferred(CustomXMLParserProcessor.SUCCESS);
        MockFlowFile out = testRunner.getFlowFilesForRelationship(CustomXMLParserProcessor.SUCCESS).get(0);
        Assert.assertEquals(out.getAttribute("foo"), "This is a text example,This is another text example");
        Assert.assertEquals(out.getAttribute("bar"), "ABC,XYZ,123");
        Assert.assertEquals(out.getAttribute("buzz"), "hello world");
    }

    @Test
    public void testInvalidXML() {
        testRunner.enqueue("<not-valid-xml>");
        testRunner.run();
        testRunner.assertAllFlowFilesTransferred(CustomXMLParserProcessor.FAILURE);
    }

    @Test
    public void testInvalidXPathQuery() {
        InputStream content = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.xml");
        testRunner.enqueue(content);
        testRunner.setProperty("foo", "JWd923-1!!-3 =dw-=d");
        testRunner.run();
        testRunner.assertAllFlowFilesTransferred(CustomXMLParserProcessor.FAILURE);
    }
}
