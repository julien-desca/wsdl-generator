package com.jdesca.wsdlgenerator.entity.wsdlElement;

import com.jdesca.wsdlgenerator.entity.Operation;
import java.util.ArrayList;
import java.util.List;


public class WsdlDefinition {
    
    private String targetNamespace;
    private String serviceName;
    
    /*
     CHILD ELEMENTS
    */
    private List<WsdlMessage> wsdlMessage;
    private WsdlPortType wsdlPortType;
    private WsdlBinding wsdlBinding;
    private WsdlService wsdlService;

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<WsdlMessage> getWsdlMessage() {
        return wsdlMessage;
    }

    public WsdlPortType getWsdlPortType() {
        return wsdlPortType;
    }

    public WsdlBinding getWsdlBinding() {
        return wsdlBinding;
    }

    public WsdlService getWsdlService() {
        return wsdlService;
    }
    
    
    

    public WsdlDefinition(String targetNamespace, String serviceName) {
        this.targetNamespace = targetNamespace;
        this.serviceName = serviceName;
        
        this.wsdlMessage = new ArrayList<WsdlMessage>();
        this.wsdlPortType = new WsdlPortType();
    }
    
    public void addOperation(Operation operation){
        //generate request and response message
        WsdlMessage wsdlMessageIn = new WsdlMessage();
        wsdlMessageIn.setName(operation.getOperationName() + "Request");
        wsdlMessageIn.getParts().add(new WsdlPart("parameterIn", ""));
        
        WsdlMessage wsdlMessageOut = new WsdlMessage();
        wsdlMessageOut.setName(operation.getOperationName() + "Response");
        wsdlMessageOut.getParts().add(new WsdlPart("parameterOut", ""));
        
        this.wsdlMessage.add(wsdlMessageIn);
        this.wsdlMessage.add(wsdlMessageOut);
        
        //add operation portType
        this.wsdlPortType.addOperation(operation, wsdlMessageIn, wsdlMessageOut, targetNamespace);
    }
    
    
}
