package com.jdesca.wsdlgenerator;

import com.jdesca.wsdlgenerator.entity.Operation;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlDefinition;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlMessage;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlOperation;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlOperation_Binding;
import com.jdesca.wsdlgenerator.entity.wsdlElement.WsdlPart;
import com.jdesca.wsdlgenerator.entity.xsd.XsdImportedSchema;
import java.io.File;
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

public class WsdlGenerator {

    
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Service name: ");
        String serviceName = scanner.nextLine();
        System.out.println("Target namespace: ");
        String targetNamespace = scanner.nextLine();
        System.out.println("Endpoint location:");
        String endpointlocation = scanner.nextLine();
        //make targetNamespace ending with "/"
        if(targetNamespace.charAt(targetNamespace.length()-1) != '/'){
            targetNamespace = targetNamespace.concat("/");
        }
        WsdlDefinition def = new WsdlDefinition(targetNamespace, serviceName, endpointlocation);
        
        String response = promptMenu(scanner);
        switch(response){
            case "1":
                break;
            case "2":
                XsdImportedSchema schema = new XsdImportedSchema();
                System.out.println("Please enter the namespace (default:"+targetNamespace+"schema/)");
                String schemaNamespace = scanner.nextLine();
                schemaNamespace = schemaNamespace.trim().equals("") ? targetNamespace+"schema/" : schemaNamespace;
                schema.setNamespace(schemaNamespace);
                System.out.println("Please enter the path of your XSD file:");
                String path = scanner.nextLine();
                schema.setSchemaLocation(path);
                def.setImportSchema(schema);
                //parse file
                //TODO
                break;
            default:
                break;
        }

        //get the service infos:
        //get the namespace name
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

    public static String promptMenu(Scanner scanner) {
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
            if(definition.getImportSchema() != null){
                root.setAttribute("xmlns:schema", definition.getImportSchema().getNamespace());
            }

            //xs schema element
            Element wsdlType = doc.createElement("wsdl:types");
            Element schema = doc.createElement("xs:schema");
            if(definition.getImportSchema() != null){
                Element xsImport = doc.createElement("xs:import");
                xsImport.setAttribute("namespace", definition.getImportSchema().getNamespace());
                xsImport.setAttribute("schemaLocation", definition.getImportSchema().getSchemaLocation());
                schema.appendChild(xsImport);
            }
            wsdlType.appendChild(schema);
            root.appendChild(wsdlType);

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
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
