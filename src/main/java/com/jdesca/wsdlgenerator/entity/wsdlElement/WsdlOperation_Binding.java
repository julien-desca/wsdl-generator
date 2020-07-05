package com.jdesca.wsdlgenerator.entity.wsdlElement;


public class WsdlOperation_Binding {
    private String name;
    
    private SoapOperation soapOperation;
    
    private String targetNamespace;

    public WsdlOperation_Binding() {
    }

    WsdlOperation_Binding(String operationName, String targetNamespace) {
        this.name = operationName;
        this.targetNamespace = targetNamespace;
        this.soapOperation = new SoapOperation();
        this.soapOperation.setSoapAction(targetNamespace+operationName);
    }

    public String getName() {
        return name;
    }

    public SoapOperation getSoapOperation() {
        return soapOperation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSoapOperation(SoapOperation soapOperation) {
        this.soapOperation = soapOperation;
    }
    
    
}
