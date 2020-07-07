package com.jdesca.wsdlgenerator;

import java.io.ByteArrayInputStream;
import java.io.File;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xmlunit.validation.Languages;
import org.xmlunit.validation.ValidationProblem;
import org.xmlunit.validation.ValidationResult;
import org.xmlunit.validation.Validator;

public class WsdlGeneratorFunctionalTest {

    @Test
    /**
     * Just give some inputs, and make sure the generated XML is valid
     */
    void functionalTest() {
        StringBuilder inputString = new StringBuilder();
        inputString.append("TestService") //service name
                .append(System.lineSeparator())
                .append("http://www.jdesca.com/TestService/")//Target namespace
                .append(System.lineSeparator())
                .append("http://localhost:8080/TestService/")//endpoint location
                .append(System.lineSeparator())
                .append("2") //create or import XSD
                .append(System.lineSeparator())
                .append(System.lineSeparator()) //schema namesapce by default
                .append("src/test/test.xsd") //schema location
                .append(System.lineSeparator())
                .append("testOperation1")//operation name
                .append(System.lineSeparator())
                .append("schema:permanenceType")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("schema:permanenceType")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("testOperation2")//operation name
                .append(System.lineSeparator())
                .append("schema:permanenceType")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("schema:permanenceType")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(System.lineSeparator());
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.toString().getBytes());

        WsdlGenerator.wsdlGeneration(in, System.out);

        Validator v = Validator.forLanguage(Languages.W3C_XML_SCHEMA_NS_URI);
        Source sources[] = {
            new StreamSource(new File("/home/julien/Dev/TOOLS/xsd_schema/wsdl.xsd")),
            new StreamSource(new File("/home/julien/Dev/TOOLS/xsd_schema/soap.xsd")),};
        v.setSchemaSources(sources);
        for (ValidationProblem problem : v.validateSchema().getProblems()) {
            System.out.println(problem.getLine() + " " + problem.getMessage());
        }
        ValidationResult vr = v.validateInstance(new StreamSource(new File("TestService.wsdl")));
        for (ValidationProblem problem : vr.getProblems()) {
            System.out.println(problem.getMessage());
        }
        Assertions.assertTrue(vr.isValid());
    }

}
