package com.jdesca.wsdlgenerator.entity.wsdlElement;


public class SoapBinding {
    private String  style;
    private String transport;

    public SoapBinding() {
        this.style = "document";
        this.transport = "http://schemas.xmlsoap.org/soap/http";
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }
    
    
    
    
}
