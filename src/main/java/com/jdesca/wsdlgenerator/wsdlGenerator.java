package com.jdesca.wsdlgenerator;

import com.jdesca.wsdlgenerator.entity.Operation;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlDefinition;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlMessage;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlOperation;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlOperation_Binding;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlPart;
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
            for (WsdlOperation wsdlOp : definition.getWsdlPortType().getWsdlOperations()) {
                Element operation = doc.createElement("wsdl:operation");
                operation.setAttribute("name", wsdlOp.getName());
                Element input = doc.createElement("wsdl:input");
                input.setAttribute("message", "tns:" + wsdlOp.getInput().getMessage());
                operation.appendChild(input);
                Element output = doc.createElement("wsdl:output");
                output.setAttribute("message", "tns:" + wsdlOp.getOutput().getMessage());
                operation.appendChild(output);
                portType.appendChild(operation);
            }
            root.appendChild(portType);

            Element wsdlBinding = doc.createElement("wsdl:binding");
            wsdlBinding.setAttribute("name", definition.getWsdlBinding().getName());
            wsdlBinding.setAttribute("type", "tns:".concat(definition.getServiceName()));
            Element soapBinding = doc.createElement("soap:binding");
            soapBinding.setAttribute("style", definition.getWsdlBinding().getSoapBinding().getStyle());
            soapBinding.setAttribute("transport", definition.getWsdlBinding().getSoapBinding().getTransport());
            wsdlBinding.appendChild(soapBinding);

            for (WsdlOperation_Binding op : definition.getWsdlBinding().getOperations()) {
                Element wsdloperation = doc.createElement("wsdl:operation");
                wsdloperation.setAttribute("name", op.getName());

                Element soapOperation = doc.createElement("soap:operation");
                soapOperation.setAttribute("soapAction", op.getSoapOperation().getSoapAction());
                wsdloperation.appendChild(soapOperation);

                Element wsdlInput = doc.createElement("wsdl:input");
                Element soapBody = doc.createElement("soap:body");
                soapBody.setAttribute("use", "literal");
                wsdlInput.appendChild(soapBody);

                Element wsdlOut = doc.createElement("wsdl:output");
                Element soapBody2 = doc.createElement("soap:body");
                soapBody2.setAttribute("use", "literal");
                wsdlOut.appendChild(soapBody2);

                wsdloperation.appendChild(wsdlInput);
                wsdloperation.appendChild(wsdlOut);

                wsdlBinding.appendChild(wsdloperation);
            }

            root.appendChild(wsdlBinding);

            //WSDL:SERVICE
            Element wsdlService = doc.createElement("wsdl:service");
            wsdlService.setAttribute("name", definition.getWsdlService().getName());
            Element wsdlPort = doc.createElement("wsdl:port");
            wsdlPort.setAttribute("binding", definition.getWsdlService().getWsdlPort().getBinding());
            wsdlPort.setAttribute("name", definition.getWsdlService().getWsdlPort().getName());
            Element wsdlAdress = doc.createElement("wsdl:address");
            wsdlAdress.setAttribute("location", definition.getWsdlService().getWsdlPort().getAddress().getLocation());
            wsdlPort.appendChild(wsdlAdress);
            wsdlService.appendChild(wsdlPort);
            root.appendChild(wsdlService);

            doc.appendChild(root);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
