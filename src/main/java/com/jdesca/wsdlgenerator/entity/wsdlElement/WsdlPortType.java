package com.jdesca.wsdlgenerator.entity.wsdlElement;

import com.jdesca.wsdlgenerator.entity.Operation;
import java.util.ArrayList;
import java.util.List;


public class WsdlPortType {
    private String name;
    
    private List<WsdlOperation> wsdlOperations;

    public WsdlPortType() {
        super();
        this.wsdlOperations = new ArrayList<WsdlOperation>();
    }

    public WsdlPortType(String name) {
        this();
        this.name = name;
    }

    void addOperation(Operation operation, WsdlMessage wsdlMessageIn, WsdlMessage wsdlMessageOut, String targetNamespace) {
        
        WsdlOperation wsdlOperation = new WsdlOperation();
        wsdlOperation.setName(operation.getOperationName());
        wsdlOperation.setInput(new wsdlInput("tns:"+wsdlMessageIn.getName()));
        wsdlOperation.setOutput(new WsdlOutput("tns:"+wsdlMessageOut.getName()));
        this.wsdlOperations.add(wsdlOperation);
    }

    public String getName() {
        return name;
    }

    public List<WsdlOperation> getWsdlOperations() {
        return wsdlOperations;
    }
    
    
    
    
    
    
    
}
