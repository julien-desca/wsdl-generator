package com.jdesca.wsdlgenerator.entity.wsdlElement;

/**
 * Logical representation of <wsdl:part> tag
 */
public class WsdlPart {

    private String name;
    
    private String type;

    public WsdlPart(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public WsdlPart() {
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
    
    
}
