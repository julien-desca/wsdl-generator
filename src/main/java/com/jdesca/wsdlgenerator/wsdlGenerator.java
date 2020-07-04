package com.jdesca.wsdlgenerator;

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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class wsdlGenerator {

    public static void main(String[] args) {

        try {

            Scanner scanner = new Scanner(System.in);

            //get the service infos:
            //get the namespace name
            System.out.println("Service name: ");
            String serviceName = scanner.nextLine();
            System.out.println("Target namespace: ");
            String targetNamespace = scanner.nextLine();
            List<String> operations = new ArrayList<String>();
            while (true) {
                System.out.println("Enter a new operation name:");
                String operationName = scanner.nextLine();
                if (operationName.equals("")) {
                    break;
                }
                operations.add(operationName);

            }

            //create XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            //root element
            Element root = doc.createElement("wsdl:definitions");
            root.setAttribute("name", serviceName);
            root.setAttribute("xmlns:wsdl", "http://schemas.xmlsoap.org/wsdl/");
            root.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
            root.setAttribute("xmlns:tns", targetNamespace);
            root.setAttribute("targetNamespace", targetNamespace);
            
            //xs cschema element
            Element schema = doc.createElement("xs:schema");
            root.appendChild(schema);

            //add operationElements (messages, portType, binding)
            List<Element> messagesElements = new ArrayList<Element>();
            List<Element> operationsElements = new ArrayList<Element>();
            List<Element> bindingOperationsElement = new ArrayList<Element>();
            for (String operation : operations) {
                //message
                Element messageIn = doc.createElement("wsdl:message");
                messageIn.setAttribute("name", operation + "Request");
                Element part = doc.createElement("wsdl:part");
                part.setAttribute("name", "parameterIn");
                messageIn.appendChild(part);
                messagesElements.add(messageIn);

                Element messageOut = doc.createElement("wsdl:message");
                messageOut.setAttribute("name", operation + "Response");
                Element part2 = doc.createElement("wsdl:part");
                part2.setAttribute("name", "parameterOut");
                messageOut.appendChild(part2);
                messagesElements.add(messageOut);

                //portType operation
                Element portTypeOperation = doc.createElement("wsdl:operation");
                portTypeOperation.setAttribute("name", operation);
                
                Element input = doc.createElement("wsdl:input");
                input.setAttribute("message", "tns:"+messageIn.getAttribute("name"));
                portTypeOperation.appendChild(input);
                
                Element output = doc.createElement("wsdl:output");
                output.setAttribute("message", "tns:"+messageOut.getAttribute("name"));
                portTypeOperation.appendChild(output);
                
                operationsElements.add(portTypeOperation);
                
                //binding operation
                Element bindingOperation = doc.createElement("wsdl:operation");
                bindingOperation.setAttribute("name", operation);
                Element soapOpElement = doc.createElement("soap:operation");
                soapOpElement.setAttribute("soapAction", targetNamespace+"/"+operation);
                bindingOperation.appendChild(soapOpElement);
                
                Element inputElement = doc.createElement("wsdl:input");
                Element inputBody = doc.createElement("soap:body");
                inputBody.setAttribute("use", "literal");
                inputElement.appendChild(inputBody);
                
                Element outputElement = doc.createElement("wsdl:output");
                Element outputBody = doc.createElement("soap:body");
                outputBody.setAttribute("use", "literal");
                outputElement.appendChild(outputBody);
                bindingOperation.appendChild(inputElement);
                bindingOperation.appendChild(outputElement);
                
                bindingOperationsElement.add(bindingOperation);
            }

            //add operation elements to root
            for (Element message : messagesElements) {
                root.appendChild(message);
            }
            
            //portType tag
            Element portType = doc.createElement("wsdl:portType");
            portType.setAttribute("name", serviceName);
            for(Element operation: operationsElements){
                portType.appendChild(operation);
            }
            root.appendChild(portType);
            
            //binding tag
            Element binding = doc.createElement("wsdl:binding");
            binding.setAttribute("name", serviceName+"SOAP");
            binding.setAttribute("type", "tns:"+serviceName);
            
            Element soapBinding = doc.createElement("soap:binding");
            soapBinding.setAttribute("style", "document");
            soapBinding.setAttribute("transport", "http://schemas.xmlsoap.org/soap/http");
            binding.appendChild(soapBinding);
            
            for(Element wsdlOperation : bindingOperationsElement){
                binding.appendChild(wsdlOperation);
            }

            //wsdl:service tag
            Element service = doc.createElement("wsdl:service");
            service.setAttribute("name", serviceName);
            Element port = doc.createElement("wsdl:port");
            port.setAttribute("binding", "tns:"+serviceName+"SOAP");
            port.setAttribute("name", serviceName+"SOAP");
            Element address = doc.createElement("wsdl:address");
            address.setAttribute("location", "http://localhost:8080/"+ serviceName);
            port.appendChild(address);
            service.appendChild(port);
            root.appendChild(service);
            //append root to doc
            doc.appendChild(root);
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");    
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(serviceName.concat(".wsdl")));
            transformer.transform(source, result);

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

}
