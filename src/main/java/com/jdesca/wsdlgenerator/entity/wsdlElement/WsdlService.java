package com.jdesca.wsdlgenerator.entity.wsdlElement;


public class WsdlService {
    private String name;
    
    WsdlPort wsdlPort;

    public WsdlService(String serviceName, WsdlBinding binding, String addressLocation) {
        this.name = serviceName;
        this.wsdlPort = new WsdlPort(binding,addressLocation);
    }

    public String getName() {
        return name;
    }

    public WsdlPort getWsdlPort() {
        return wsdlPort;
    }
    
    
    
    
}
