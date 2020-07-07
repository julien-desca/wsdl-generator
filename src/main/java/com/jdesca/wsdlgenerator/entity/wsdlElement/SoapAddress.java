package com.jdesca.wsdlgenerator.entity.wsdlElement;


public class SoapAddress {
    private String location;

    public SoapAddress(String location) {
        this.location = location;
    }

    public SoapAddress() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    
}
