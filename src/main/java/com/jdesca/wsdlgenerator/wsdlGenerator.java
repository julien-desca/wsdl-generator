package com.jdesca.wsdlgenerator;

import com.jdesca.wsdlgenerator.entity.Operation;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlDefinition;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlMessage;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlOperation;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlPart;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlPortType;
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

        Scanner scanner = new Scanner(System.in);

        //get the service infos:
        //get the namespace name
        System.out.println("Service name: ");
        String serviceName = scanner.nextLine();
        System.out.println("Target namespace: ");
        String targetNamespace = scanner.nextLine();
        WsdlDefinition def = new WsdlDefinition(targetNamespace, serviceName);
        while (true) {
            System.out.println("Enter a new operation name:");
            String operationName = scanner.nextLine();
            if (operationName.equals("")) {
                break;
            }
            def.addOperation(new Operation(operationName));
        }

        toWSDL(def, def.getServiceName() + ".wsdl");

    }

    public static void toWSDL(WsdlDefinition definition, String fileName) {

        try {
            //create XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            //root element
            Element root = doc.createElement("wsdl:definitions");
            root.setAttribute("name", definition.getServiceName());
            root.setAttribute("xmlns:wsdl", "http://schemas.xmlsoap.org/wsdl/");
            root.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
            root.setAttribute("xmlns:tns", definition.getTargetNamespace());
            root.setAttribute("targetNamespace", definition.getTargetNamespace());

            //xs schema element
            // @TODO
            Element schema = doc.createElement("xs:schema");
            root.appendChild(schema);

            List<Element> messagesElements = new ArrayList<Element>();
            for (WsdlMessage wsdlMessage : definition.getWsdlMessage()) {
                Element message = doc.createElement("wsdl:message");
                message.setAttribute("name", wsdlMessage.getName());
                for (WsdlPart wsdlpart : wsdlMessage.getParts()) {
                    Element part = doc.createElement("wsdl:part");
                    part.setAttribute("name", wsdlpart.getName());
                    part.setAttribute("type", wsdlpart.getType());
                    message.appendChild(part);
                }
                root.appendChild(message);
            }
            
            Element portType = doc.createElement("wsdl:portType");
            portType.setAttribute("name", definition.getWsdlPortType().getName());
            for(WsdlOperation wsdlOp : definition.getWsdlPortType().getWsdlOperations()){
                Element operation = doc.createElement("wsdl:operation");
                operation.setAttribute("name", wsdlOp.getName());
                Element input = doc.createElement("wsdl:input");
                input.setAttribute("message", "tns:"+wsdlOp.getInput().getMessage());
                operation.appendChild(input);
                Element output = doc.createElement("wsdl:output");
                output.setAttribute("message", "tns:"+wsdlOp.getOutput().getMessage());
                operation.appendChild(output);
                portType.appendChild(operation);
            }
            root.appendChild(portType);
            
            doc.appendChild(root);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//            //add operationElements (messages, portType, binding)
//            List<Element> operationsElements = new ArrayList<Element>();
//            List<Element> bindingOperationsElement = new ArrayList<Element>();
//            for (String operation : operations) {
//                //portType operation
//                Element portTypeOperation = doc.createElement("wsdl:operation");
//                portTypeOperation.setAttribute("name", operation);
//                
//                Element input = doc.createElement("wsdl:input");
//                input.setAttribute("message", "tns:"+messageIn.getAttribute("name"));
//                portTypeOperation.appendChild(input);
//                
//                Element output = doc.createElement("wsdl:output");
//                output.setAttribute("message", "tns:"+messageOut.getAttribute("name"));
//                portTypeOperation.appendChild(output);
//                
//                operationsElements.add(portTypeOperation);
//                
//                //binding operation
//                Element bindingOperation = doc.createElement("wsdl:operation");
//                bindingOperation.setAttribute("name", operation);
//                Element soapOpElement = doc.createElement("soap:operation");
//                soapOpElement.setAttribute("soapAction", targetNamespace+"/"+operation);
//                bindingOperation.appendChild(soapOpElement);
//                
//                Element inputElement = doc.createElement("wsdl:input");
//                Element inputBody = doc.createElement("soap:body");
//                inputBody.setAttribute("use", "literal");
//                inputElement.appendChild(inputBody);
//                
//                Element outputElement = doc.createElement("wsdl:output");
//                Element outputBody = doc.createElement("soap:body");
//                outputBody.setAttribute("use", "literal");
//                outputElement.appendChild(outputBody);
//                bindingOperation.appendChild(inputElement);
//                bindingOperation.appendChild(outputElement);
//                
//                bindingOperationsElement.add(bindingOperation);
//            }
//
//            //add operation elements to root
//            for (Element message : messagesElements) {
//                root.appendChild(message);
//            }
//            
//            //portType tag
//            Element portType = doc.createElement("wsdl:portType");
//            portType.setAttribute("name", serviceName);
//            for(Element operation: operationsElements){
//                portType.appendChild(operation);
//            }
//            root.appendChild(portType);
//            
//            //binding tag
//            Element binding = doc.createElement("wsdl:binding");
//            binding.setAttribute("name", serviceName+"SOAP");
//            binding.setAttribute("type", "tns:"+serviceName);
//            
//            Element soapBinding = doc.createElement("soap:binding");
//            soapBinding.setAttribute("style", "document");
//            soapBinding.setAttribute("transport", "http://schemas.xmlsoap.org/soap/http");
//            binding.appendChild(soapBinding);
//            for(Element wsdlOperation : bindingOperationsElement){
//                binding.appendChild(wsdlOperation);
//            }
//            root.appendChild(binding);
//
//            //wsdl:service tag
//            Element service = doc.createElement("wsdl:service");
//            service.setAttribute("name", serviceName);
//            Element port = doc.createElement("wsdl:port");
//            port.setAttribute("binding", "tns:"+serviceName+"SOAP");
//            port.setAttribute("name", serviceName+"SOAP");
//            Element address = doc.createElement("wsdl:address");
//            address.setAttribute("location", "http://localhost:8080/"+ serviceName);
//            port.appendChild(address);
//            service.appendChild(port);
//            root.appendChild(service);
//            //append root to doc
//            doc.appendChild(root);
//            // write the content into xml file
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");    
//            DOMSource source = new DOMSource(doc);
//            StreamResult result = new StreamResult(new File(serviceName.concat(".wsdl")));
//            transformer.transform(source, result);
//
//            // Output to console for testing
//            StreamResult consoleResult = new StreamResult(System.out);
//            transformer.transform(source, consoleResult);
//        } catch (Exception ex) {
//            System.err.println(ex.getMessage());
//        }
}
