package com.jdesca.wsdlgenerator.entity.wsdlElement;


abstract public class WsdlInOut {

    protected String message;

    public WsdlInOut() {
    }

    public WsdlInOut(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
    
}
