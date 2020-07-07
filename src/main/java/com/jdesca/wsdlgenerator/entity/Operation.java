package com.jdesca.wsdlgenerator.entity;

import java.util.ArrayList;
import java.util.List;


public class Operation {
    
    private String operationName;
    private List<String> inTypes;
    private List<String> outTypes;
    

    public Operation(String operationName) {
        this.operationName = operationName;
        this.inTypes = new ArrayList<>();
        this.outTypes  = new ArrayList<>();
    }
    
    public String getOperationName(){
        return this.operationName;
    }

    public List<String> getInTypes() {
        return inTypes;
    }
    
    public List<String> getOutTypes() {
        return outTypes;
    }
    
    
    
    
    
    
}
