package com.jdesca.wsdlgenerator.entity.wsdlElement;


public class WsdlPort {
    String binding;
    String name;
    
    SoapAddress address;

    public WsdlPort(WsdlBinding binding, String addresLocation) {
        this.name = binding.getName();
        this.binding = "tns:".concat(binding.getName());
        this.address = new SoapAddress(addresLocation);
    }

    public String getBinding() {
        return binding;
    }

    public String getName() {
        return name;
    }

    public SoapAddress getAddress() {
        return address;
    }
    
    
    
    
}
