package com.jdesca.wsdlgenerator.entity.wsdlElement;


public class WsdlPort {
    String binding;
    String name;
    
    WsdlAddress address;

    public WsdlPort(WsdlBinding binding, String addresLocation) {
        this.name = binding.getName();
        this.binding = "tns:".concat(binding.getName());
        this.address = new WsdlAddress(addresLocation);
    }

    public String getBinding() {
        return binding;
    }

    public String getName() {
        return name;
    }

    public WsdlAddress getAddress() {
        return address;
    }
    
    
    
    
}
