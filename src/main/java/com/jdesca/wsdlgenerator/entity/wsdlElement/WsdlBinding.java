package com.jdesca.wsdlgenerator.entity.wsdlElement;

import com.jdesca.wsdlgenerator.entity.Operation;
import java.util.ArrayList;
import java.util.List;


public class WsdlBinding {
    private String name;
    
    private String type;
    
    private List<WsdlOperation_Binding> operations;
    
    private SoapBinding soapBinding;

    public WsdlBinding() {
        this.operations = new ArrayList<>();
        this.soapBinding = new SoapBinding();
    }

    public WsdlBinding(String name, String type) {
        this();
        this.name = name;
        this.type = type;
        
    }
    
    public void addOperation(Operation operation){
    }

    void addOperation(Operation operation, String targetNamespace) {
        WsdlOperation_Binding wsdlOperation = new WsdlOperation_Binding(operation.getOperationName(),targetNamespace);
        this.operations.add(wsdlOperation);
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<WsdlOperation_Binding> getOperations() {
        return operations;
    }

    public void setOperations(List<WsdlOperation_Binding> operations) {
        this.operations = operations;
    }

    public SoapBinding getSoapBinding() {
        return soapBinding;
    }

    public void setSoapBinding(SoapBinding soapBinding) {
        this.soapBinding = soapBinding;
    }
    
    
    
    
}
