package com.jdesca.wsdlgenerator.entity;


public class Operation {
    private String operationName;
    
    public String getOperationName(){
        return this.operationName;
    }

    public Operation(String operationName) {
        this.operationName = operationName;
    }
    
    
}
