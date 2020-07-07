package com.jdesca.wsdlgenerator;

import com.jdesca.wsdlgenerator.entity.Operation;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlDefinition;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlMessage;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlOperation;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlOperation_Binding;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlPart;
import com.jdesca.wsdlgenerator.entity.xsd.XsdImportedSchema;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WsdlGenerator {

    public static void main(String[] args) {
        try {

            Scanner scanner = new Scanner(System.in);

            System.out.println("Service name: ");
            String serviceName = scanner.nextLine();
            System.out.println("Target namespace: ");
            String targetNamespace = scanner.nextLine();
            System.out.println("Endpoint location:");
            String endpointlocation = scanner.nextLine();
            //make targetNamespace ending with "/"
            if (targetNamespace.charAt(targetNamespace.length() - 1) != '/') {
                targetNamespace = targetNamespace.concat("/");
            }
            WsdlDefinition def = new WsdlDefinition(targetNamespace, serviceName, endpointlocation);

            List<String> types = new ArrayList<>();
            
            String response = promptMenuXSD(scanner);
            switch (response) {
                case "1":
                    break;
                case "2":
                    XsdImportedSchema schema = new XsdImportedSchema();
                    System.out.println("Please enter the namespace (default:" + targetNamespace + "schema/)");
                    String schemaNamespace = scanner.nextLine();
                    schemaNamespace = schemaNamespace.trim().equals("") ? targetNamespace + "schema/" : schemaNamespace;
                    schema.setNamespace(schemaNamespace);
                    System.out.println("Please enter the path of your XSD file:");
                    String path = scanner.nextLine();
                    schema.setSchemaLocation(path);
                    def.setImportSchema(schema);

                    //parse file
                    File fXmlFile = new File(schema.getSchemaLocation());
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(fXmlFile);

                    //optional, but recommended
                    //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
                    doc.getDocumentElement().normalize();

                    String tagName = doc.getDocumentElement().getTagName();
                    String prefix = tagName.split(":")[0];

                    NodeList nList = doc.getDocumentElement().getChildNodes();

                    for (int temp = 0; temp < nList.getLength(); temp++) {

                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element eElement = (Element) nNode;
                            if(eElement.getTagName().equals(prefix.concat(":element"))){
                                types.add(eElement.getAttribute("name"));
                            }

                        }
                    }

                    break;
                default:
                    break;
            }
            
            while (true) {
                System.out.println("Enter a new operation name:");
                String operationName = scanner.nextLine();
                if (operationName.equals("")) {
                    break;
                }
                Operation operation = new Operation(operationName);
                while(true){
                    System.out.println("add a type for the input (empty to go to output type:");
                    String opType = scanner.nextLine();
                    if(opType.trim().equals("")){
                        break;
                    }
                    operation.getInTypes().add(opType);
                }while(true){
                    System.out.println("add a type for the input (empty to go to new operation:");
                    String opType = scanner.nextLine();
                    if(opType.trim().equals("")){
                        break;
                    }
                    operation.getOutTypes().add(opType);
                }
                def.addOperation(operation);
            }

            WsdlFileBuilder.toWSDL(def, def.getServiceName() + ".wsdl");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String promptMenuXSD(Scanner scanner) {
        while (true) {
            System.out.println("Do you want to create the xsd schema or do you want to import an existing one?");
            System.err.println("1. Create new XSD schema");
            System.err.println("2. Import an existing XSD schema");
            String Uresponse = scanner.nextLine();
            switch (Uresponse) {
                case "1":
                case "2":
                    return Uresponse;
                default:
                    System.out.println("Please choose one of the options bellow");
                    break;
            }
        }
    }


}
